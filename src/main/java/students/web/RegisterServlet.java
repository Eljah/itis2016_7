package students.web;

import org.apache.log4j.Logger;
import students.dao.RegisteredUserDAO;
import students.logic.Group;
import students.logic.ManagementSystem;
import students.logic.RegisteredUser;
import students.logic.Student;
import students.util.MailUtil;
import students.web.forms.MainFrameForm;
import students.web.forms.StudentForm;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Ilya Evlampiev on 05.10.2015.
 */
public class RegisterServlet extends HttpServlet
{
    private RegisteredUserDAO userDAO=new RegisteredUserDAO();
    static Logger log = Logger.getLogger(RegisterServlet.class);

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        //processRequest(req, resp);
        log.info("Starting registering user");
        RegisteredUser newUser=new RegisteredUser();
        log.debug("Retrieving user name from session");
        newUser.setUsername(req.getParameter("username"));
        try {
            newUser.setPassword(req.getParameter("password"));
            log.debug("Calculating and setting password for the user");
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5 algorithm not fount");
            e.printStackTrace();
        }
        newUser.setEmail(req.getParameter("email"));
        log.debug("Retrieving user email from session");
        log.debug("Saving user "+newUser.getUsername());
        if (userDAO.addUser(newUser,"role1")){
            log.info("Saving user "+newUser.getUsername()+" succeed");
            //req.setAttribute("passwordhash", newUser.getPasswordHash());
            getServletContext().getRequestDispatcher("/userDetails.jsp").forward(req, resp);
            //getServletContext().getRequestDispatcher("/logon.jsp").forward(req, resp);
            MailUtil.sendGreetingEmailTo(newUser.getEmail(),newUser.getUsername(),req.getParameter("password"));
            log.info("Sending email to "+newUser.getEmail());
        }
        else
        {
            log.error("Saving user "+newUser.getUsername()+" failed");
            getServletContext().getRequestDispatcher("/register.jsp").forward(req, resp);
        }


    }

    // Переопределим стандартные методы
    public void doPost(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException {
        processRequest(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

}
