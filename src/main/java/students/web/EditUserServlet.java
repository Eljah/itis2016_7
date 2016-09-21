package students.web;

import org.apache.log4j.Logger;
import students.dao.RegisteredUserDAO;
import students.logic.RegisteredUser;
import students.util.MailUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 * Created by Ilya Evlampiev on 07.10.2015.
 */
@MultipartConfig(maxFileSize = 16177215) // upload file up to 16MB
public class EditUserServlet extends HttpServlet
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
            if (req.getParameter("password")!=null) newUser.setPassword(req.getParameter("password"));
            if (req.getParameter("passwordHash")!=null) newUser.setPasswordHash(req.getParameter("passwordHash"));
            log.debug("Calculating and setting password for the user");
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5 algorithm not fount");
            e.printStackTrace();
        }
        newUser.setEmail(req.getParameter("email"));
        log.debug("Retrieving user email from session");

        // obtains the upload file part in this multipart request
        InputStream inputStream = null;
        Part filePart = req.getPart("photo");
        if (filePart != null) {
            // debug messages
            System.out.println(filePart.getName());
            System.out.println(filePart.getSize());
            System.out.println(filePart.getContentType());

            // obtains input stream of the upload file
            inputStream = filePart.getInputStream();
        }


        log.debug("Saving user "+newUser.getUsername());
        if (userDAO.updateUser(req.getParameter("oldusername"), newUser, "role1",inputStream)){
            log.info("Saving user "+newUser.getUsername()+" succeed");
            //req.setAttribute("passwordhash", newUser.getPasswordHash());
            getServletContext().getRequestDispatcher("/userDetails.jsp").forward(req, resp);
            MailUtil.sendGreetingEmailTo(newUser.getEmail(), newUser.getUsername(), req.getParameter("password"));
            log.info("Sending email to "+newUser.getEmail());
        }
        else
        {
            log.error("Saving user "+newUser.getUsername()+" failed");
            getServletContext().getRequestDispatcher("/userDetails.jsp").forward(req, resp);
        }


    }

    // Переопределим стандартные методы
    public void doPost(HttpServletRequest req, HttpServletResponse resp)  throws ServletException, IOException {
        processRequest(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            RegisteredUser user = userDAO.getUserByName(req.getUserPrincipal().getName());
            log.info("getting current user name " + req.getUserPrincipal().getName());
            //req.setAttribute("username",user.getUsername());
            //req.setAttribute("email",user.getEmail());
            //req.setAttribute("password",user.getPasswordHash());
            getServletContext().getRequestDispatcher("/userDetails.jsp?username=" + user.getUsername() + "&email=" + user.getEmail()+ "&passwordHash=" + user.getPasswordHash()+ "&password=").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}