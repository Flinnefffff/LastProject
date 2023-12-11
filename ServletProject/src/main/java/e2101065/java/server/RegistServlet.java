package e2101065.java.server;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/RegistServlet")
public class RegistServlet extends HttpServlet {
    private Connection conn;
    private PreparedStatement ps;
    private int resultSet;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        String dbUser = context.getInitParameter("db_user");
        String dbPassword = context.getInitParameter("db_password");

        try {
            // Load the JDBC driver and establish a connection
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mariadb://mariadb.vamk.fi/e2101065_finalproject", "e2101065", "DZCtWC5pEC2");
            
            // Prepare the SQL statement for registration
            ps = conn.prepareStatement("INSERT INTO Students (name, username, password) VALUES(?, ?, ?)");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String name = req.getParameter("name");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        RequestDispatcher reqDis;

        // Validate username format
        if (!isValidUsername(username)) {
            reqDis = req.getRequestDispatcher("register.html");
            req.setAttribute("message", "Invalid username format");
            reqDis.include(req, res);
            return; // Exit the method, no need to proceed further
        }

        try {
            // Check if the username already exists in the database
            PreparedStatement checkUsernamePS = conn.prepareStatement("SELECT * FROM Students WHERE username = ?");
            checkUsernamePS.setString(1, username);
            ResultSet usernameResult = checkUsernamePS.executeQuery();

            if (usernameResult.next()) {
                // Username already exists, treat it as unsuccessful
                reqDis = req.getRequestDispatcher("login.html");
                req.setAttribute("message", "Username already exists");
                reqDis.include(req, res);
            } else {
                // Username doesn't exist, proceed with registration
                ps.setString(1, name);
                ps.setString(2, username);
                ps.setString(3, password);
                resultSet = ps.executeUpdate();

                if (resultSet > 0) {
                    // Registration success
                    req.setAttribute("message", "Registration successfully");
                    RequestDispatcher reqDis1 = req.getRequestDispatcher("login.html");
                    reqDis1.forward(req, res);
                } else {
                    // Registration unsuccessful
                    reqDis = req.getRequestDispatcher("RegistServlet");
                    req.setAttribute("message", "Registration unsuccessfully");
                    reqDis.include(req, res);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().println("An error occurred during registration");
        }
    }

    private boolean isValidUsername(String username) {
        // Implement your username validation logic here
        // For example, you can check if the username follows certain criteria
        return true; // Replace with your validation logic
    }

    @Override
    public void destroy() {
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
