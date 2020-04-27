package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;

public class ViewController extends TypeofUser{ //view code borrowed from https://stackoverflow.com/questions/18941093/how-to-fill-up-a-tableview-with-database-data and my assignment 2
    public Button mainmenubutton;
    public TableView tv1;
    public ObservableList<ObservableList> data;
    public Label labeldisplaylimits;


    public void initialize() throws ClassNotFoundException, SQLException  {
        labeldisplaylimits.setText("As a(n) " + getUserType() + " you are allowed " + getmedialimit() + " media items and " + getitemlimit() + " total items"); //still broken
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


}
