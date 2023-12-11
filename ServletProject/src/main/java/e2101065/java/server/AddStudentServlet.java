package e2101065.java.server;

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

@WebServlet("/addStudentServlet")
public class AddStudentServlet extends HttpServlet {
    private Connection conn;
    private PreparedStatement ps;

    @Override
    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mariadb://mariadb.vamk.fi/e2101065_finalproject", "e2101065", "DZCtWC5pEC2");
            ps = conn.prepareStatement("INSERT INTO Students (name, username, password) VALUES(?, ?, ?)");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String name = req.getParameter("name");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            // Check if the username already exists in the database
            PreparedStatement checkUsernamePS = conn.prepareStatement("SELECT * FROM Students WHERE username = ?");
            checkUsernamePS.setString(1, username);
            ResultSet usernameResult = checkUsernamePS.executeQuery();

            if (usernameResult.next()) {
                // Username already exists, student creation failed
                PrintWriter out = res.getWriter();
                out.println("Student creation failed. Username '" + username + "' already exists.");
            } else {
                // Username doesn't exist, proceed with student creation
                ps.setString(1, name);
                ps.setString(2, username);
                ps.setString(3, password);
                int result = ps.executeUpdate();
                PrintWriter out = res.getWriter();
                out.println("Student " + result + " created");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().println("Username already exists");
        }
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