package e2101065.java.server;
 import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {

    private Connection conn;

    @Override
    public void init() throws ServletException {
        // Establish database connection in the init method
        try {
            Class.forName("org.mariadb.jdbc.Driver"); // Replace with your database driver
            conn = DriverManager.getConnection("jdbc:mariadb://mariadb.vamk.fi/e2101065_finalproject","e2101065","DZCtWC5pEC2");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT password FROM Students WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String PasswordFromDB = rs.getString("password");
                // Use a secure method to compare hashed passwords (e.g., BCrypt)
                // Replace this comparison with your actual password comparison method
                if (password.equals(PasswordFromDB)) {
                    // Authentication successful, create a session and redirect
                    request.getSession().setAttribute("username", username);
                    response.sendRedirect(request.getContextPath() + "/studentAdministration.html");
                    return;
                } else {
                    // Incorrect password
                	 response.sendRedirect(request.getContextPath() + "/login.html");
                    return;
                }
            } else {
                // Email not found in the database
            	 response.sendRedirect(request.getContextPath() + "/login.html");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Authentication failed due to an unknown reason
        response.sendRedirect("/login.html?error=authentication_failed");
    } 
    }
