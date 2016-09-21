package students.web;

import org.apache.log4j.Logger;
import students.dao.RegisteredUserDAO;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

@WebServlet("/avatar.jpg")
public class DisplayBlobServlet extends HttpServlet {
    RegisteredUserDAO userDAO = new RegisteredUserDAO();
    static Logger log = Logger.getLogger(DisplayBlobServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException {
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("image/jpeg");
        try {
            Blob bl = userDAO.getBlob(request.getParameter("username"));
            InputStream in = bl.getBinaryStream();
            int length = (int) bl.length();
            log.info("the length of blob file is " + length);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            while ((length = in.read(buffer)) != -1) {
                log.trace("writing " + length + " bytes");
                out.write(buffer, 0, length);
            }

            in.close();
            out.flush();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}