package sample;

import java.sql.*;

// This Assignment is Submitted by Colton Weber
public class Controller {
    public void initialize() {
        try{
            final String DB_URL = "jdbc:derby://localhost:1527/LibraryDB;";
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();

            stmt.execute("create table LIBRARY\n" +
                    "(\n" +
                    "    ISBN   INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n" +
                    "    TITLE_OF_MEDIA  VARCHAR(99),\n" +
                    "    AUTHOR          VARCHAR(40),\n" +
                    "    LOCATION        VARCHAR(20),\n" +
                    "    QUANTITY        INTEGER\n" +
                    ")");
            stmt.execute("alter table LIBRARY\n" +
                    "    add constraint LIBRARY_PK\n" +
                    "        primary key (ISBN)\n");
            stmt.close();
            System.out.println("Success!..Table created");
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Table already exists");
        }
    }
}
