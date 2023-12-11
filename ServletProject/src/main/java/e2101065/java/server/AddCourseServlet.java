package e2101065.java.server;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/addCourseServlet")
public class AddCourseServlet extends HttpServlet {
    private Connection conn;
    private PreparedStatement ps;

    @Override
    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mariadb://mariadb.vamk.fi/e2101065_finalproject", "e2101065", "DZCtWC5pEC2");
            ps = conn.prepareStatement("INSERT INTO Courses (name, teacher) VALUES(?, ?)");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String name = req.getParameter("name");
        String teacher = req.getParameter("teacher");

        try {
            ps.setString(1, name);
            ps.setString(2, teacher);
            int result = ps.executeUpdate();
            PrintWriter out = res.getWriter();
            out.println("Course " + result + " created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try {
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
