package students.web.login;

import org.apache.log4j.Logger;
import students.dao.RegisteredUserDAO;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Created by Ilya Evlampiev on 11.10.2015.
 */

@WebServlet("/prelogin")
public class PreLoginServlet extends HttpServlet {
    RegisteredUserDAO userDAO = new RegisteredUserDAO();
    static Logger log = Logger.getLogger(PreLoginServlet.class);
    static String COOKIE_NAME = "studentsUUID";
    static int COOKIE_AGE = 2592000; //in seconds; 30 days

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException, ServletException {
        doProcess(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException {
        doProcess(request, response);
    }

    public void doProcess(HttpServletRequest request,
                          HttpServletResponse response
    )
            throws IOException, ServletException

    {
        String uuid = getCookieValue(request, COOKIE_NAME);
        Object user = request.getSession().getAttribute("user");
        String username = null;

        if (uuid == null)
        {if (user != null) {
            //request.login(user.getUsername(), user.getPassword());
            //request.getSession().setAttribute("user", user); // Login.
            log.info("The user is defined from session as: " + user.toString());
            addCookie(response, COOKIE_NAME, uuid, COOKIE_AGE); // Extends age.
            log.info("Cookie was added and time prolonged ");
        } else {
            log.info("The user is not defined from session as");
            removeCookie(response, COOKIE_NAME);
            log.info("Cookie is removed and request is dispatched to the logon.jsp");
            getServletContext().getRequestDispatcher("/logon.jsp").forward(request, response);
        }
        }

        else  {
            try {
                username = userDAO.findUsernameByUUID(uuid);
                log.info("The cookie for the student app accepted; the corresponding user fount: " + username);
                request.login(username, uuid);
                log.info("The user " + username + " was requested to log in using username and uuid received from cookie");
                addCookie(response, COOKIE_NAME, uuid, COOKIE_AGE); // Extends age.
                log.info("Cookie was added and time prolonged ");
                PrintWriter pw=response.getWriter();
                pw.print("<body onLoad=\"window.location.reload()\"/>");

            } catch (SQLException e) {
                e.printStackTrace();
                getServletContext().getRequestDispatcher("/logon.jsp").forward(request, response);
            } catch (Exception e)
            {
                e.printStackTrace();
                removeCookie(response, COOKIE_NAME);
                getServletContext().getRequestDispatcher("/logon.jsp").forward(request, response);
            }
        }


    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName()) && cookie.getValue()!=null && cookie.getValue()!="") {
                    log.info("Cookie sent: " + cookie.getName());
                    return cookie.getValue();
                }
            }
        }
        return null;
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
