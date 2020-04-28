package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.sql.*;


public class ViewController extends TypeofUser{
    public Button mainmenubutton;
    public TableView tv1;
    public ObservableList<ObservableList> data;
    public Label labeldisplaylimits;
    public Button add;
    public Button cart;
    public TextField textfieldISBN;
    public Label erroroverlimit;
    public Label erroritemcheck;
    public Label errorquantitycheck;
    public Label errorcheckedout;
    public Button viewcheckedoutbutton;
    private int mediacount = 0;
    private int itemcount = 0;


    public void initialize() throws ClassNotFoundException, SQLException  { //inserting a database table into a javafx table view code borrowed from https://stackoverflow.com/questions/18941093/how-to-fill-up-a-tableview-with-database-data and my assignment 2
        if (getUserType().equals("faculty")){
            labeldisplaylimits.setText("As a(n) " + getUserType() + " you are allowed " + "infinite" + " media items and " + "infinite" + " total items");
        }
        else{
            labeldisplaylimits.setText("As a(n) " + getUserType() + " you are allowed " + getmedialimit() + " media items and " + getitemlimit() + " total items");
        }

        data = FXCollections.observableArrayList();
        final String DB_URL = "jdbc:mysql://db2.cma4gd0of8tf.us-east-2.rds.amazonaws.com/sche";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(DB_URL, "WeberUH", "rootadmin");

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * from LIBRARY");
            ResultSet rs = stmt.executeQuery();
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>, ObservableValue<String>>(){
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tv1.getColumns().addAll(col);
                System.out.println("Column ["+i+"] ");
            }
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }
            tv1.setItems(data);
            getUsersCheckedOut(getUserName());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }

    public void add() throws ClassNotFoundException, SQLException {
        String I = textfieldISBN.getText();
        int ISBN = Integer.parseInt(I.trim());
        final String DB_URL = "jdbc:mysql://db2.cma4gd0of8tf.us-east-2.rds.amazonaws.com/sche";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(DB_URL, "WeberUH", "rootadmin");
        PreparedStatement stmt = null;
        stmt = conn.prepareStatement("select ISBN, Type_of_media, Quantity from LIBRARY where ISBN = ?");
        stmt.setInt(1, ISBN);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        String mediaType = rs.getString("Type_of_Media");
        int Quantity = rs.getInt("Quantity");
        int medialimit = getmedialimit();
        int itemlimit = getitemlimit();
        if (!getCheckedOutArray().contains(ISBN)) {
            errorcheckedout.setVisible(false);
            if (Quantity > 0) {
                errorquantitycheck.setVisible(false);
                if (!getcartarray().contains(ISBN)) {
                    erroritemcheck.setVisible(false);
                    if (mediaType.equals("Media")) {
                        if (medialimit > mediacount || medialimit == -1) {
                            mediacount++;
                            getcartarray().add(ISBN);
                            erroroverlimit.setVisible(false);
                        } else {
                            erroroverlimit.setVisible(true);
                        }
                    } else if (mediaType.equals("Book")) {
                        if (itemlimit > itemcount || itemlimit == -1) {
                            itemcount++;
                            getcartarray().add(ISBN);
                            erroroverlimit.setVisible(false);
                        } else {
                            erroroverlimit.setVisible(true);
                        }
                    }
                } else {
                    erroritemcheck.setVisible(true);
                }
            } else {
                errorquantitycheck.setVisible(true);
            }
        }
        else {
            errorcheckedout.setVisible(true);
        }
    }

    public void getUsersCheckedOut(String username) throws ClassNotFoundException, SQLException {
        final String DB_URL = "jdbc:mysql://db2.cma4gd0of8tf.us-east-2.rds.amazonaws.com/sche";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(DB_URL, "WeberUH", "rootadmin");
        PreparedStatement stmt = conn.prepareStatement("Select ISBN from CHECKEDOUT where CougarID = ?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int ISBN = rs.getInt("ISBN");
            getCheckedOutArray().add(ISBN);
        }
    }

    public void mainmenu() throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage stage = (Stage) mainmenubutton.getScene().getWindow();
        Scene scene = new Scene(root2);
        stage.setScene(scene);
    }

    public void cart() throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("cart.fxml"));
        Stage stage = (Stage) cart.getScene().getWindow();
        Scene scene = new Scene(root2);
        stage.setScene(scene);
    }

    public void viewcheckedoutbutton() throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("checkedout.fxml"));
        Stage stage = (Stage) viewcheckedoutbutton.getScene().getWindow();
        Scene scene = new Scene(root2);
        stage.setScene(scene);
    }
}