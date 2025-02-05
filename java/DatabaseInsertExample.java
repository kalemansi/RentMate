import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseInsertExample {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mansi";
        String username = "root";
        String password = "Manasi@123";

        try {
            // Load the MySQL JDBC driver (optional for JDBC 4.0 and later)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            Connection con = DriverManager.getConnection(url, username, password);
            System.out.println("Connection established");

            // Create the SQL INSERT statement
            String sql = "INSERT INTO Users (column1, column2) VALUES (?, ?)";
            PreparedStatement st = con.prepareStatement(sql);

            // Set the parameter values
            st.setString(1, "value1");
            st.setString(2, "value2");

            // Execute the statement
            int rowsInserted = st.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new user was inserted successfully!");
            }

            // Close the resources
            st.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}


    