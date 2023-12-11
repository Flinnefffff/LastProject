package e2101065.java.server;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/deleteCourseServlet")
public class DeleteCourseServlet extends HttpServlet {

    private Connection connection;
    private PreparedStatement statement;

    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            // Assign to the class variable instead of the parameter
            connection = DriverManager.getConnection("jdbc:mariadb://mariadb.vamk.fi/e2101065_finalproject", "e2101065", "DZCtWC5pEC2");
            statement = connection.prepareStatement("DELETE FROM Courses WHERE course_id = ? AND name = ? AND teacher = ?");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get parameters from the form
        String course_id = request.getParameter("course_id");
        String name = request.getParameter("name");
        String teacher = request.getParameter("teacher");

        try {
            statement.setString(1, course_id);
            statement.setString(2, name);
            statement.setString(3, teacher);

            int result = statement.executeUpdate();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print("<html><body><b>Course " + result + " deleted</b></body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
