package students.web;

import com.google.gwt.dev.ModuleTabPanel;
import org.apache.log4j.Logger;
import students.dao.NoteDao;
import students.logic.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Ilya Evlampiev on 06.10.2015.
 */
public class NoteServlet extends HttpServlet {
    static Logger log = Logger.getLogger(RegisterServlet.class);
    static NoteDao noteDao = new NoteDao();

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        RegisteredUser user = new RegisteredUser();
        user.setUsername(req.getUserPrincipal().getName());
        Note note = new Note();
        note.setText(req.getParameter("text"));
        note.setUser(user);
        noteDao.addNote(note);
        log.debug("Storin note to the db");
        try {
            List<Note> noteList=noteDao.getNotes();
            PrintWriter pr = resp.getWriter();
            for (Note n : noteList) {
                    pr.write("<i>" + n.getUser().getUsername()+"</i> "+n.getText()+"<br>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

