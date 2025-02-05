import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/Sign_upServlet")
public class Sign_upServlet extends HttpServlet {
    @Override

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()){
      
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            int rowsInserted;
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sign", "root", "Manasi@123")) {
                String query = "insert into signup1 VALUES (?,?,?,?)";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, username);
                pst.setString(3, password);
                pst.setString(4, role); // Save role in the database
                rowsInserted = pst.executeUpdate();
            

            if (rowsInserted > 0) {
    if ( null == role) {
        // Handle other roles, if needed
        response.sendRedirect("login.jsp?signup=success");
    } else          // If signup is successful, redirect the user to the login page
                    switch (role) {
                        case "renter":
                            // If role is renter, redirect to login page
                            response.sendRedirect("login.jsp?signup=success");
                            break;
                        case "owner":
                            // If role is owner, redirect to login page
                            response.sendRedirect("login.jsp?signup=success");
                            break;
                        default:
                            // Handle other roles, if needed
                            response.sendRedirect("login.jsp?signup=success");
                            break;
                    }
} 

        } catch (Exception e) {
            out.println(e);
            /*response.sendRedirect("sign-up.jsp?error=db");*/
        }
    }       catch (ClassNotFoundException ex) {
                Logger.getLogger(Sign_upServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
}

}
}
    
