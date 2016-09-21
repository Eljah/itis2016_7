package students.web.login;

import org.apache.log4j.Logger;
import students.dao.RegisteredUserDAO;
import students.logic.RegisteredUser;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by Ilya Evlampiev on 11.10.2015.
 */

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    RegisteredUserDAO userDAO = new RegisteredUserDAO();
    static Logger log = Logger.getLogger(LoginServlet.class);
    static String COOKIE_NAME="studentsUUID";
    static int COOKIE_AGE=2592000; //in seconds; 30 days

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {

        String username = request.getParameter("j_username");
        String password = request.getParameter("j_password");
        log.info("Username "+username+" password values are obtained from the form sent");
        boolean remember = "on".equals(request.getParameter("rememberme"));
        log.info("Remember me option is set to " + remember);
        try {
            RegisteredUser user=new RegisteredUser();
            user.setUsername(username);
            try {
                user.setPassword(password);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            log.info("The hashed password for user "+username+" is calculated and will be passed (as hashed) to .login() ");
            //request.login(user.getUsername(), user.getPasswordHash()); //for JAAS realm
            request.login(username, password); //for JDBC realm
            log.info("If user succeed with login for username "+username+" the random UUID is generated");
            String uuid = UUID.randomUUID().toString();
            try {
                if (remember) {
                    userDAO.setUUID(uuid, username);
                    log.info("For user  "+username+" uuid is stored to db");
                    addCookie(response, COOKIE_NAME, uuid, COOKIE_AGE);
                    log.info("For user "+username+" uuid cookie is added to the forwarded response");
                }
                else
                {
                    userDAO.deleteUUID(username);
                    log.info("For user "+username+" uuid is cleared from the db");
                    removeCookie(response, COOKIE_NAME);
                    log.info("For user "+username+" uuid cookie is removed i.e. set to 0 age in forwarded response");
                }
                } catch (SQLException e) {
                e.printStackTrace();
            }
            response.sendRedirect(request.getParameter("url"));
            log.info("For user "+username+" the page is redirected to the initial url user tried to access");

        } catch (ServletException sevex) {
            request.setAttribute("error",sevex.getLocalizedMessage());
            log.info("Some error occurs for "+username+"; the error message is "+sevex.getLocalizedMessage());
            getServletContext().getRequestDispatcher("/logon.jsp").forward(request, response);
        }
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/studentsApp/");
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    public static void removeCookie(HttpServletResponse response, String name) {
        addCookie(response, name, null, 0);
    }
}
