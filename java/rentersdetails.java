import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet("/rentersdetails")
public class rentersdetails extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        String additionalComments = request.getParameter("additionalComments");
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sign","root","Manasi@123");
            String sql = "INSERT INTO renters1 (name,dob,contact_number,email,national_id,employment_status,employer_name,monthly_income,rental_history,emergency_contact_name,emergency_contact_number,rental_agreement,additional_comments) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,fullName);
            pstmt.setString(2,dob);
            pstmt.setString(3,contactNumber);
            pstmt.setString(4,email);
            pstmt.setString(5,nationalId);
            pstmt.setString(6,employmentStatus);
            pstmt.setString(7,employerName);
            pstmt.setString(8,monthlyIncome);
            pstmt.setString(9,rentalHistory);
            pstmt.setString(10,emergencyContactName);
            pstmt.setString(11,emergencyContactNumber);
            pstmt.setString(12,rentalAgreement);
            pstmt.setString(13,additionalComments);
            
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                response.getWriter().println("Data inserted successfully.");
            } else {
                response.getWriter().println("Failed to insert data.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}