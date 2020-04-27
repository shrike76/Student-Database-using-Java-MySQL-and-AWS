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

public class AdminController {


    public Button mainmenubutton;
    public TableView tv1;
    public ObservableList<ObservableList> data;
    public TextField textfieldTitle;
    public RadioButton radioBook;
    public RadioButton radioMedia;
    public TextField textfieldCallNumber;
    public TextField textfieldQuantity;
    public Label labelError;
    public Button create;
    public TextField textfieldAuthor;
    public Button buttonuserswitch;


    public void initialize() throws ClassNotFoundException, SQLException {
        data = FXCollections.observableArrayList();
        final String DB_URL = "jdbc:mysql://db2.cma4gd0of8tf.us-east-2.rds.amazonaws.com/sche";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(DB_URL, "WeberUH", "rootadmin");

        view();

        create.setOnAction(ActionEvent ->{
            try {
                String title = textfieldTitle.getText();
                String Author = textfieldAuthor.getText();
                String Location = textfieldCallNumber.getText();
                String Quantity = textfieldQuantity.getText(); //maybe force integer here
                PreparedStatement stmt = null;
                stmt = conn.prepareStatement("INSERT INTO LIBRARY ( Title_of_Media, Author, Location, Type_of_media, Quantity) VALUES (?,?,?,?,?)");
                stmt.setString(1, title);
                stmt.setString(2, Author);
                stmt.setString(3, Location);
                if (radioBook.isSelected()){
                    stmt.setString(4, "Book");
                }
                else{
                    stmt.setString(4, "Media");
                }
                stmt.setString(5, Quantity);
                stmt.executeUpdate();
                System.out.println("record inserted into table");
                view();
        } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void mainmenu() throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Stage stage = (Stage) mainmenubutton.getScene().getWindow();
        Scene scene = new Scene(root2);
        stage.setScene(scene);
    }

    public void userSwitch() throws IOException {
        Parent root2 = FXMLLoader.load(getClass().getResource("Users.fxml"));
        Stage stage = (Stage) buttonuserswitch.getScene().getWindow();
        Scene scene = new Scene(root2);
        stage.setScene(scene);
    }

    public void view(){
        try {
            data = FXCollections.observableArrayList();
            final String DB_URL = "jdbc:mysql://db2.cma4gd0of8tf.us-east-2.rds.amazonaws.com/sche";
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, "WeberUH", "rootadmin");
            PreparedStatement stmt = conn.prepareStatement("SELECT * from LIBRARY");
            ResultSet rs = stmt.executeQuery();
            if (tv1.getItems().isEmpty()){
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }
}
