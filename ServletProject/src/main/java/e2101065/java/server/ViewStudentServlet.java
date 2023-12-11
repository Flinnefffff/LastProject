package e2101065.java.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/student")
public class ViewStudentServlet extends HttpServlet {
    private Connection conn;

    @Override
    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mariadb://mariadb.vamk.fi/e2101065_finalproject", "e2101065",
                    "DZCtWC5pEC2");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Student List</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h2>Student List</h2>");

            List<Student> students = getStudentList();

            if (students.isEmpty()) {
                out.println("<p>No students available</p>");
            } else {
                out.println("<ul>");
                for (Student student : students) {
                    out.println("<li>" + student.getName() + " - " + student.getUsername() + "</li>");
                }
                out.println("</ul>");
            }

            out.println("</body>");
            out.println("</html>");
        }
    }

    private List<Student> getStudentList() {
        List<Student> students = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Students");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String name = rs.getString("name");
                String username = rs.getString("username");

                Student student = new Student(studentId, name, username);
                students.add(student);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    @Override
    public void destroy() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
