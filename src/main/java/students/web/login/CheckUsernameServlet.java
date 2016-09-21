package students.web.login;

import students.dao.RegisteredUserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Created by Ilya Evlampiev on 19.10.2015.
 */
@WebServlet("/checkusername")
public class CheckUsernameServlet  extends HttpServlet {
    RegisteredUserDAO userDAO = new RegisteredUserDAO();

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
        String username=request.getParameter("username");
        try {
            boolean result=userDAO.checkUserExists(username);
            if (result) {      response.setStatus(200);
                response.getWriter().write("true"); //json format );

            }
            else
            {
                response.setStatus(200);
                response.getWriter().write("\"User already exists\""); //json format );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
     /*
        response.getWriter().write("{\n" +
             "    \"isError\": \"true\",\n" +
             "    \"errorMessage\": \"The User Name you chose is already in use. Please enter another name.\"\n" +
             "}");
             */
        //response.getWriter().write("{ result: 'false' }"); //json format );

    }
}
