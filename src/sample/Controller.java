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
            getcartarray().clear();
            String user = textfieldusername.getText();
            final String DB_URL = "jdbc:mysql://db2.cma4gd0of8tf.us-east-2.rds.amazonaws.com/sche";
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, "WeberUH", "rootadmin");
            PreparedStatement stmt = conn.prepareStatement("Select * from USERS where CougarID = ?");
            stmt.setString(1, user);
            ResultSet rs = stmt.executeQuery();
            rs.last();

            if (rs.getRow() != 0) { //https://www.rgagnon.com/javadetails/java-0292.html
                rs.beforeFirst();
                rs.next();
                int item = rs.getInt("Total_Item_Limit");
                int media = rs.getInt("Media_Item_Limit");
                String userType = rs.getString("User_Type");
                setItemlimit(item);
                setmedialimit(media);
                setUserName(user);
                setUserType(userType);

                if (userType.equals("admin")) { //admin
                    Parent root2 = FXMLLoader.load(getClass().getResource("admin.fxml"));
                    Stage stage = (Stage) login.getScene().getWindow();
                    Scene scene = new Scene(root2);
                    stage.setScene(scene);
                }
                else{
                    Parent root3 = FXMLLoader.load(getClass().getResource("view.fxml"));
                    Stage stage = (Stage) login.getScene().getWindow();
                    Scene scene = new Scene(root3);
                    stage.setScene(scene);
                }
            }
            else{
                invalidlabel.setVisible(true);
            }



        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
