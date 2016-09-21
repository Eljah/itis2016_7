package students.web;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Ilya Evlampiev on 27.10.2015.
 */
@WebServlet("/encode")
public class TestEncodinServlet extends HttpServlet {
    static Logger log = Logger.getLogger(TestEncodinServlet.class);


    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        log.debug("User list is got from the db");
        String text=req.getParameter("text");
        System.out.println(text);
        resp.getWriter().write("Введено было значение "+text+"<BR>"
                +"<form action=\"encode\" method=\"post\"><input type=\"text\" name=\"text\" ></input></form> ");


        // /getServletContext().getRequestDispatcher("/users.jsp").forward(req, resp);    }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //req.setCharacterEncoding("ISO-8859-1");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        log.debug("User list is got from the db");
        String text=req.getParameter("text");
        System.out.println(text);
        String newText= new String (text.getBytes ("ISO-8859-1"), "UTF-8");//=URLDecoder.decode(text, "utf8");
        System.out.println(newText);
        resp.getWriter().write("Введено было значение "+text+"<BR>"
                +"<form action=\"encode\" method=\"post\"><input type=\"text\" name=\"text\" ></input></form> ");


        // /getServletContext().getRequestDispatcher("/users.jsp").forward(req, resp);    }
    }
}

