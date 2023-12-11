package e2101065.java.server;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/studentJoinCourseServlet")
public class studentJoinCourseServlet extends HttpServlet {

    private Connection conn;
    private PreparedStatement ps;

    @Override
    public void init() throws ServletException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mariadb://mariadb.vamk.fi/e2101065_finalproject", "e2101065", "DZCtWC5pEC2");
            ps = conn.prepareStatement("INSERT INTO Student_Course (student_id, course_id) VALUES(?, ?)");
        } catch (SQLException | ClassNotFoundException e) {
            throw new ServletException("Error initializing servlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String studentIdParam = req.getParameter("studentId");
        String courseIdParam = req.getParameter("courseId");

        try {
            int studentId = Integer.parseInt(studentIdParam);
            int courseId = Integer.parseInt(courseIdParam);

            ps.setInt(1, studentId);
            ps.setInt(2, courseId);

            int rowsInserted = ps.executeUpdate();

            res.setContentType("text/html");
            res.getWriter().println("<html><body>");

            if (rowsInserted > 0) {
                res.setStatus(HttpServletResponse.SC_OK);
                res.getWriter().println("<p>Success to join course</p>");
            } else {
                res.setStatus(HttpServletResponse.SC_NOT_FOUND);
                res.getWriter().println("<p>Fail to join course</p>");
            }

            res.getWriter().println("</body></html>");
        } catch (NumberFormatException | SQLException e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.getWriter().println("<p>Error processing the request</p>");
            e.printStackTrace();
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
