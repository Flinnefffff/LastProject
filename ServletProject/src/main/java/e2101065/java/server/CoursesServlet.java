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
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/courses")
public class CoursesServlet extends HttpServlet {

    private Connection connection;
    private PreparedStatement statement;

    @Override
    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mariadb://mariadb.vamk.fi/e2101065_finalproject", "e2101065", "DZCtWC5pEC2");
            statement = connection.prepareStatement("SELECT * FROM Courses");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            ResultSet resultSet = statement.executeQuery();

            out.println("<html><head><title>Courses</title></head><body>");
            out.println("<h2>Courses</h2>");

            while (resultSet.next()) {
                int course_id = resultSet.getInt("course_id");
                String name = resultSet.getString("name");
                String teacher = resultSet.getString("teacher");

                out.println("<p>Course_id: " + course_id + " Name: " + name + " Teacher: " + teacher + "</p>");
            }

            out.println("</body></html>");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}