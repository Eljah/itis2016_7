package students.web;

import org.apache.log4j.Logger;
import students.dao.RegisteredUserDAO;
import students.logic.Group;
import students.logic.ManagementSystem;
import students.logic.RegisteredUser;
import students.logic.Student;
import students.util.MailUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Ilya Evlampiev on 06.10.2015.
 */
public class StudentListServlet extends HttpServlet {
    static Logger log = Logger.getLogger(RegisterServlet.class);

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text / html;charset=UTF-8");
        Group gr = new Group();
        gr.setGroupId(Integer.parseInt(req.getParameter("groupId")));
        Collection<Student> students = new ArrayList<Student>();
        try {
            students = ManagementSystem.getInstance().getStudentsFromGroup(gr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.debug("Retrieving user name from session");
        PrintWriter pr = resp.getWriter();
        for (Student st : students) {
            pr.write("<br>" + st.getSurName());
        }

    }

    // Переопределим стандартные методы
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

}
