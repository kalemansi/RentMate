import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class Rentersdetails1 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String fullName = request.getParameter("fullName");
        String dob = request.getParameter("dob");
        String contactNumber = request.getParameter("contactNumber");
        String email = request.getParameter("email");
        String nationalId = request.getParameter("nationalId");
        String employmentStatus = request.getParameter("employmentStatus");
        String employerName = request.getParameter("employerName");
        String monthlyIncome = request.getParameter("monthlyIncome");
        String rentalHistory = request.getParameter("rentalHistory");
        String emergencyContactName = request.getParameter("emergencyContactName");
        String emergencyContactNumber = request.getParameter("emergencyContactNumber");
        String rentalAgreement = request.getParameter("rentalAgreement");
        

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sign", "root", "Manasi@123");

            String query = "SELECT * FROM renters1";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1,fullName);
            pst.setString(2,dob);
            pst.setString(3,contactNumber);
            pst.setString(4,email);
            pst.setString(5,nationalId);
            pst.setString(6,employmentStatus);
            pst.setString(7,employerName);
            pst.setString(8,monthlyIncome);
            pst.setString(9,rentalHistory);
            pst.setString(10,emergencyContactName);
            pst.setString(11,emergencyContactNumber);
            pst.setString(12,rentalAgreement);
            

            int rowsInserted = pst.executeUpdate();
            pst.close();
            con.close();

            // Redirect to owner dashboard after successful submission
            if (rowsInserted > 0) {
                response.sendRedirect("owner.html");
            } else {
                response.getWriter().println("Error: Could not insert renter details.");
            }

        } catch (ClassNotFoundException | SQLException e) {
            response.getWriter().println("Database connection failed: " + e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sign", "root", "Manasi@123");

            String query = "SELECT * FROM renters1";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Owner Dashboard</title>");
            out.println("<link rel='stylesheet' href='dashboard.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Renter Details</h2>");
            out.println("<table border='1'>");
            out.println("<tr>");
            out.println("<th>fullName</th><th>DOB</th><th>ContactNumber</th><th>Email</th><th>National ID</th><th>Employment</th><th>Income</th><th>Rental History</th><th>Emergency Contact Name</th><th> Emergency Contact Number</th><th>Agreement Signed</th></tr>");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString(1) + "</td>");
                out.println("<br>");
                out.println("<td>" + rs.getString(2) + "</td>");
                out.println("<br>");
                out.println("<td>" + rs.getString(3) + "</td>");
                out.println("<br>");
                out.println("<td>" + rs.getString(4) + "</td>");
                out.println("<br>");
                out.println("<td>" + rs.getString(5) + "</td>");
                out.println("<br>");
                out.println("<td>" + rs.getString(6) + "</td>");
                out.println("<br>");
                out.println("<td>" + rs.getString(7) + "</td>");
                out.println("<br>");
                out.println("<td>" + rs.getString(8) + "</td>");
                out.println("<br>");
                out.println("<td>" + rs.getString(9) + " - " + rs.getString(10) + "</td>");
                out.println("<br>");
                out.println("<td>" + rs.getString(11) + "</td>");
                out.println("<br>");
                out.println("<td>" + rs.getString(12) + "</td>");
                out.println("<br>");
                
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body>");
            out.println("</html>");

            rs.close();
            pst.close();
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
