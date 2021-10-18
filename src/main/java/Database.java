import java.sql.*;

public class Database {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    Database(DatabaseNames databaseName) {
        try {
            this.connection = DriverManager.getConnection("jdbc:" + Constants.host + databaseName, Constants.user, Constants.password);
            System.out.println("Connection established!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
