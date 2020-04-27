package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

// This Assignment is Submitted by Colton Weber
public class Controller extends TypeofUser{
    public TextField textfieldusername;
    public TextField textfieldpassword;
    public Label invalidlabel;
    public Button login;

    public void login() {
        try {
            String user = textfieldusername.getText();
            if (user.equals("1111111")) { //under graduates
                setUserType("undergraduate");
                setItemlimit(5);
                setmedialimit(3);
                System.out.println(getUserType() + getitemlimit() + getmedialimit());
            } else if (user.equals("1111112")) { //graduates
                setUserType("graduate");
                setItemlimit(8);
                setmedialimit(6);
            } else if (user.equals("1111113")) { //faculty
                setUserType("faculty");
                setItemlimit(-1);
                setmedialimit(-1);
            } else if (user.equals("1111114")) { //staff
                setUserType("staff");
                setItemlimit(8);
                setmedialimit(6);
            } else if (user.equals("1111115")) { //Alumni
                setUserType("Alumni");
                setItemlimit(4);
                setmedialimit(2);
            } else if (user.equals("admin")) { //admin
                Parent root2 = FXMLLoader.load(getClass().getResource("admin.fxml"));
                Stage stage = (Stage) login.getScene().getWindow();
                Scene scene = new Scene(root2);
                stage.setScene(scene);
            } else {
                invalidlabel.setVisible(true);
            }
            Parent root3 = FXMLLoader.load(getClass().getResource("view.fxml"));
            Stage stage = (Stage) login.getScene().getWindow();
            Scene scene = new Scene(root3);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void initialize() {
        try{
            final String DB_URL = "jdbc:mysql://db2.cma4gd0of8tf.us-east-2.rds.amazonaws.com/sche";
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, "WeberUH", "rootadmin");
            Statement stmt = conn.createStatement();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Table already exists");
        }
    }
}
