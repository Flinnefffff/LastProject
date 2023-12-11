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

@WebServlet("/deleteStudentServlet")
public class DeleteStudentServlet extends HttpServlet {

    private Connection connection;
    private PreparedStatement statement;

    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mariadb://mariadb.vamk.fi/e2101065_finalproject", "e2101065", "DZCtWC5pEC2");
            statement = connection.prepareStatement("DELETE FROM Students WHERE student_id = ? AND name = ? AND username = ?");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String student_id = request.getParameter("student_id");
        String name = request.getParameter("name");
        String username = request.getParameter("username"); // Corrected parameter name

        try {
            statement.setInt(1, Integer.parseInt(student_id)); // Parse student_id as an integer
            statement.setString(2, name);
            statement.setString(3, username);

            int result = statement.executeUpdate();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.print("<b>Student " + result + " deleted");
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
