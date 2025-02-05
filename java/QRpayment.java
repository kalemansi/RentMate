import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/QRpayment")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class QRpayment extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/sign";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Manasi@123";

    private static final Logger LOGGER = Logger.getLogger(QRpayment.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL JDBC Driver

            try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT * FROM properties1";
                PreparedStatement pst = con.prepareStatement(query);
                ResultSet rs = pst.executeQuery();

                out.println("<!DOCTYPE html>");
                out.println("<html lang='en'>");
                out.println("<head>");
                out.println("<meta charset='UTF-8'>");
                out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                out.println("<title>QR Payment</title>");
                out.println("<link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css' rel='stylesheet'>");
                out.println("</head>");
                out.println("<body>");
                out.println("<div class='container mt-5'>");
                out.println("<h2 class='mb-4'>Available QR Codes</h2>");

                while (rs.next()) {
                    String propertyName = rs.getString("propertyName");
                    String qrImage = rs.getString("Scanner_Image");

                    out.println("<div class='room-item card p-3 mb-4'>");
                    out.println("<h4>" + propertyName + "</h4>");
                    out.println("<img src='images/" + qrImage + "' alt='QR Scanner' width='270' height='180'>");

                    out.println("<form action='QRpayment' method='POST' enctype='multipart/form-data'>");
                    out.println("<input type='hidden' name='propertyName' value='" + propertyName + "'>");

                    out.println("<div class='mb-3'>");
                    out.println("<label class='form-label'>Payment Amount</label>");
                    out.println("<input type='number' class='form-control' name='paymentAmount' required>");
                    out.println("</div>");

                    out.println("<div class='mb-3'>");
                    out.println("<label class='form-label'>Payment Method</label>");
                    out.println("<select class='form-select' name='paymentMethod' required>");
                    out.println("<option value='credit_card'>Credit Card</option>");
                    out.println("<option value='bank_transfer'>Bank Transfer</option>");
                    out.println("<option value='paypal'>PayPal</option>");
                    out.println("<option value='other'>Other</option>");
                    out.println("</select>");
                    out.println("</div>");

                    out.println("<div class='mb-3'>");
                    out.println("<label class='form-label'>Upload Payment Screenshot</label>");
                    out.println("<input type='file' class='form-control' name='paymentScreenshot' accept='image/*' required>");
                    out.println("</div>");

                    out.println("<button type='submit' class='btn btn-primary'>Submit Payment</button>");
                    out.println("</form>");
                    out.println("</div>");
                }

                out.println("</div>");
                out.println("</body>");
                out.println("</html>");

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Database connection error", ex);
                response.sendRedirect("error.html");
            }
        } catch (ClassNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "JDBC Driver not found", ex);
            response.sendRedirect("error.html");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String propertyName = request.getParameter("propertyName");
        String paymentAmount = request.getParameter("paymentAmount");
        String paymentMethod = request.getParameter("paymentMethod");
        Part paymentScreenshot = request.getPart("paymentScreenshot");

        if (propertyName == null || paymentAmount == null || paymentMethod == null || paymentScreenshot == null) {
            LOGGER.log(Level.WARNING, "Missing form fields");
            response.sendRedirect("error.html");
            return;
        }

        String fileName = Paths.get(paymentScreenshot.getSubmittedFileName()).getFileName().toString();
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        String filePath = uploadPath + File.separator + fileName;
        paymentScreenshot.write(filePath);

        LOGGER.log(Level.INFO, "File uploaded to: {0}", filePath);

        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PrintWriter out = response.getWriter()) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String query = "INSERT INTO payments (propertyName, paymentAmount, paymentMethod, screenshot) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, propertyName);
            pst.setString(2, paymentAmount);
            pst.setString(3, paymentMethod);
            pst.setString(4, "uploads/" + fileName);

            int rowsAffected = pst.executeUpdate();
            LOGGER.log(Level.INFO, "Rows Affected: {0}", rowsAffected);

            if (rowsAffected > 0) {
                response.sendRedirect("success.html");
            } else {
                LOGGER.log(Level.SEVERE, "Data not inserted.");
                response.sendRedirect("error.html");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error", ex);
            response.sendRedirect("error.html");
        }
    }
}
