import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Explo_room extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        response.setContentType("text/html;charset=UTF-8");
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sign", "root", "Manasi@123");
            String query = "SELECT * FROM properties1";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html lang='en'>");
                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                out.println("<title>Explore Rooms</title>");
                out.println("<link rel='stylesheet' href='explore_room.css'>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='dashboard container'>");
                
                out.println("<div class='sidebar'>");
                out.println("<h1 class='user-title'>User</h1>");
                out.println("<ul class='tabs'>");
                out.println("<li><a href='rentersdetails.html'>Renter Details</a></li>");
                out.println("<li><a href='Payment.html'>Payment</a></li>");
                out.println("<li><a href='notification.html'>Notifications</a></li>");
                out.println("<li><a href='index.html'>Log Out</a></li>");
                out.println("</ul>");
                out.println("</div>");
                
                out.println("<div class='main-content'>");
                out.println("<h2>Explore Available Rooms</h2>");
                out.println("<p>Browse through a variety of rooms available near you.</p>");
                
                while (rs.next()) {
    out.println("<div class='room-item'>");
    out.println("<form action='rentersdetails.html' method='get'>");

    // Container to hold image and details side by side
    out.println("<div class='room-content'>"); 
    
    // Image on the left
    out.println("<div class='room-image'>");
    out.println("<img src='images/"+rs.getString("image")+"'"+"alt='Room Image' width='270' height='180'>");
    out.println("</div>");
    
    // Property details on the right
    out.println("<div class='room-details'>");
    out.println("<h3>" + rs.getString("PropertyName") + "</h3>");
    out.println("<p><strong>Type:</strong> " + rs.getString("PropertyType") + "</p>");
    out.println("<p><strong>Price:</strong> " + rs.getString("Price") + "</p>");
    out.println("<p><strong>Location:</strong> " + rs.getString("Location") + "</p>");
    out.println("<p><strong>Description:</strong> " + rs.getString("Description") + "</p>");
    
    out.println("<button type='submit'>Book Now</button>");
    out.println("</div>"); // Close room-details
    
    out.println("</div>"); // Close room-content
    
    out.println("</form>");
    out.println("</div>");
}


                
                out.println("</div>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            response.sendRedirect("gm.html");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Explo_room.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Explo_room.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
