import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/my_propertiesServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10, // 10MB
                 maxRequestSize = 1024 * 1024 * 50) // 50MB
public class my_propertiesServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Retrieve form data
        String propertyName = request.getParameter("propertyName");
        String propertyType = request.getParameter("propertyType");
        String price = request.getParameter("price");
        String location = request.getParameter("location");
        String description = request.getParameter("description"); 
        String filename="";
        String scannerImageFileName = "";
       
        
        // Handling file upload
        Part filePart = request.getPart("propertyImage");

        if (filePart != null && filePart.getSize() > 0) {
            filename =filePart.getSubmittedFileName();
            for(Part part:request.getParts())
            {
            filePart.write("C://Users/manas/OneDrive/Documents/NetBeansProjects/WebApplication1/Web/images/"+filename);
        }
        }
        
        Part scannerImagePart = request.getPart("Scanner");
        if (scannerImagePart != null && scannerImagePart.getSize() > 0) {
            scannerImageFileName = scannerImagePart.getSubmittedFileName();
            scannerImagePart.write("C://Users/manas/OneDrive/Documents/NetBeansProjects/WebApplication1/Web/images/" + scannerImageFileName);
        }
      
        //
      
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sign", "root", "Manasi@123")) {
                String query = "INSERT INTO properties1 VALUES (?,?,?,?,?,?,?)";
                PreparedStatement pst = con.prepareStatement(query);
                pst.setString(1, propertyName);
                pst.setString(2, propertyType);
                pst.setString(3, price);
                pst.setString(4, location);
                pst.setString(5, description);
                pst.setString(6, filename);
                pst.setString(7, scannerImageFileName);

                int rowsInserted = pst.executeUpdate();
                con.close();

                if (rowsInserted > 0) {
                    out.println("<script>alert('Property listed successfully!'); window.location.href='owner.html';</script>");
                } else {
                    out.println("<script>alert('Error listing property.'); window.location.href='owner.html';</script>");
                }
            }
        } catch (Exception e) {
            out.println("<script>alert('Database Error: " + e.getMessage() + "'); window.location.href='propertyListing.jsp';</script>");
        } 
    }
}
