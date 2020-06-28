package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHandler extends Configurations {
    Connection dbconnection;

    public Connection getConnection() {
        String connectionString = "jdbc:mysql://"+Configurations.localhost+":"+Configurations.portNumber+"/"+Configurations.nameDataBase;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            dbconnection = DriverManager.getConnection(connectionString, Configurations.userDataBase, Configurations.passwordDataBase);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dbconnection;
    }
}
