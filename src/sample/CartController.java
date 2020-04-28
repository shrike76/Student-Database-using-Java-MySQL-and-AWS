package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

public class CartController extends TypeofUser{
    public ObservableList<ObservableList> data;
    public Button mainmenubutton;
    public Button checkoutbutton;
    public TableView tv1;

    public void initialize() throws ClassNotFoundException, SQLException {
        data = FXCollections.observableArrayList();
        final String DB_URL = "jdbc:mysql://db2.cma4gd0of8tf.us-east-2.rds.amazonaws.com/sche";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(DB_URL, "WeberUH", "rootadmin");
        try {
            String sql = "SELECT ISBN, Title_of_Media, Author, Location, Type_of_media from LIBRARY where ISBN in (?)";
            String sqlIN = getcartarray().stream().map(x -> String.valueOf(x)).collect(Collectors.joining(",", "(", ")")); //https://mkyong.com/jdbc/jdbc-preparedstatement-sql-in-condition/
            sql = sql.replace("(?)", sqlIN);
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tv1.getColumns().addAll(col);
                System.out.println("Column [" + i + "] ");
            }
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }
            tv1.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

    public void mainmenu() throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage stage = (Stage) mainmenubutton.getScene().getWindow();
        Scene scene = new Scene(root2);
        stage.setScene(scene);
    }

    public void checkout() throws ClassNotFoundException, SQLException, IOException {
        final String DB_URL = "jdbc:mysql://db2.cma4gd0of8tf.us-east-2.rds.amazonaws.com/sche";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(DB_URL, "WeberUH", "rootadmin");
        List<Integer> cart = getcartarray();
        String username = getUserName();
        PreparedStatement stmt = null;
        for (int ISBN : cart){
            stmt = conn.prepareStatement("insert into CHECKEDOUT values (?,?);");
            stmt.setString(1, username);
            stmt.setInt(2, ISBN);
            stmt.executeUpdate();
            reduceQuantity(ISBN);
        }
        getcartarray().clear();
        Parent root2 = FXMLLoader.load(getClass().getResource("checkedout.fxml"));
        Stage stage = (Stage) checkoutbutton.getScene().getWindow();
        Scene scene = new Scene(root2);
        stage.setScene(scene);
    }

    public void reduceQuantity(int ISBN) throws ClassNotFoundException, SQLException {
        final String DB_URL = "jdbc:mysql://db2.cma4gd0of8tf.us-east-2.rds.amazonaws.com/sche";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(DB_URL, "WeberUH", "rootadmin");
        PreparedStatement stmt = conn.prepareStatement("select Quantity from LIBRARY where ISBN = ?");
        stmt.setInt(1, ISBN);
        stmt.executeQuery();
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int quantity = rs.getInt("Quantity");
        quantity--;
        stmt = conn.prepareStatement("UPDATE LIBRARY set Quantity = ? where ISBN = ?");
        stmt.setInt(1, quantity);
        stmt.setInt(2, ISBN);
        stmt.executeUpdate();
    }
}