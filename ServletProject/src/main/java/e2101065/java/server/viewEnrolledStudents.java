package e2101065.java.server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/viewEnrolledStudents")
public class viewEnrolledStudents extends HttpServlet {
    private Connection conn;

    @Override
    public void init() {
        // Initialization code for database connection
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");

        try {
            int courseId = Integer.parseInt(req.getParameter("courseId")); // Retrieve courseId from request

            // Read the content of the HTML file
            InputStream fileStream = getServletContext().getResourceAsStream("/enrolled_students.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
            StringBuilder htmlContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line);
            }

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT s.name, s.username FROM Students s INNER JOIN Student_Course sc ON s.student_id = sc.student_id WHERE sc.course_id = ?");
            ps.setInt(1, courseId);

            ResultSet rs = ps.executeQuery();

            StringBuilder studentListHTML = new StringBuilder();
            while (rs.next()) {
                String name = rs.getString("name");
                String username = rs.getString("username");
                studentListHTML.append("<li>Name: ").append(name).append(", Username: ").append(username).append("</li>");
            }

            // Inject the student list into the HTML content
            String finalHTML = htmlContent.toString().replace("<!-- Student list will be dynamically populated here -->", studentListHTML.toString());

            PrintWriter out = res.getWriter();
            out.println(finalHTML);

            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().println("An error occurred");
        }
    }

    @Override
    public void destroy() {
        // Cleanup code for closing database connection
    }
}

