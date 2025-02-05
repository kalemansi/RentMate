import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jakarta.servlet.annotation.WebServlet;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10,      // 10MB
                 maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class Payment extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String propertyName = request.getParameter("propertyName");
        if (propertyName == null || propertyName.trim().isEmpty()) {
            response.sendRedirect("error.html");
            return;
        }

        String scannerImage = "default_qr.png"; // Default image

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sign", "root", "Manasi@123");

            // Fix: Use a parameterized query instead of hardcoded 'jain'
            String query = "SELECT Scanner_Image FROM properties1 WHERE propertyName = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, propertyName);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                scannerImage = rs.getString("Scanner_Image"); // Fetch image name from DB
            }

            try (PrintWriter out = response.getWriter()) {
                out.println("<!DOCTYPE html>");
                out.println("<html lang='en'>");
                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                out.println("<title>Payment Form</title>");
                out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css' rel='stylesheet'>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='container mt-5'>");
                out.println("<h2>Payment Details for " + propertyName + "</h2>");
                out.println("<div class='row mb-3'>");
                out.println("<div class='col-md-6'>");
                out.println("<h4>Scan the QR Code to Pay</h4>");

                // Corrected image path: Assuming uploads/ is accessible from web
                out.println("<img src='uploads/" + scannerImage + "' alt='QR Scanner' width='270' height='180'>");

                out.println("</div>");
                out.println("</div>");
                out.println("<form id='paymentForm' action='Payment' method='POST' enctype='multipart/form-data'>");
                out.println("<input type='hidden' name='propertyName' value='" + propertyName + "'>");
                out.println("<div class='row mb-3'>");
                out.println("<div class='col-md-6'>");
                out.println("<label for='paymentAmount' class='form-label'>Payment Amount</label>");
                out.println("<input type='number' class='form-control' id='paymentAmount' name='paymentAmount' required>");
                out.println("</div>");
                out.println("<div class='col-md-6'>");
                out.println("<label for='paymentMethod' class='form-label'>Payment Method</label>");
                out.println("<select class='form-select' id='paymentMethod' name='paymentMethod' required>");
                out.println("<option value='credit_card'>Credit Card</option>");
                out.println("<option value='bank_transfer'>Bank Transfer</option>");
                out.println("<option value='paypal'>PayPal</option>");
                out.println("<option value='other'>Other</option>");
                out.println("</select>");
                out.println("</div>");
                out.println("</div>");
                out.println("<div class='row mb-3'>");
                out.println("<div class='col-md-12'>");
                out.println("<label for='paymentScreenshot' class='form-label'>Upload Payment Screenshot</label>");
                out.println("<input type='file' class='form-control' id='paymentScreenshot' name='paymentScreenshot' accept='image/*' required>");
                out.println("</div>");
                out.println("</div>");
                out.println("<div class='row'>");
                out.println("<div class='col-md-12 text-center'>");
                out.println("<button type='submit' class='btn btn-primary'>Submit Payment</button>");
                out.println("</div>");
                out.println("</div>");
                out.println("</form>");
                out.println("</div>");
                out.println("</body>");
                out.println("</html>");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Payment.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect("error.html");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
