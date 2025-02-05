import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet; 
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try (PrintWriter out = response.getWriter()){
      
     
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sign", "root", "Manasi@123")) {
                String query = "SELECT * FROM signup1 WHERE username=? AND password=? AND role=?";

                PreparedStatement pst = con.prepareStatement(query);
               
                pst.setString(1, username);
                pst.setString(2, password);
                pst.setString(3, role); // Save role in the database
                ResultSet rs = pst.executeQuery();
            

            if (rs.next()) {
            if ("renter".equals(role)) {
                    response.sendRedirect("user.html");
                } else {
                    response.sendRedirect("owner.html");
            }
                }else {
               out.println("Invalid credentials");
            }
        
} 

        } catch (Exception e) {
            out.println(e);
            /*response.sendRedirect("sign-up.jsp?error=db");*/
        }
    }}

}

