package students.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import students.logic.Group;
import students.logic.ManagementSystem;

public class SimpleList extends HttpServlet
{

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text / html;charset=UTF-8");
        PrintWriter pw = resp.getWriter();
        pw.println("<html><head><title>Groups</title><script src=\"js/ajax.js\"></script><script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.0.0-alpha1/jquery.js\"></script></head>");
        pw.println("<body><B> Список групп</B>");
        pw.println("<table border = 1>");
        try {
            List l = ManagementSystem.getInstance().getGroups();
            for (Iterator it = l.iterator(); it.hasNext();) {
                Group gr = (Group) it.next();
                pw.println("<tr>");
                pw.println("<td>" + gr.getGroupId() + "</td>");
                pw.println("<td>" + gr.getNameGroup() + "</td>");
                pw.println("<td>" + gr.getCurator() + "</td>");
                pw.println("<td>" + gr.getSpeciality() + "</td>");
                pw.println("<td><div id=\"stud-results"+gr.getGroupId()+"\" onclick=\"javascript:callAjax('"+gr.getGroupId()+"','"+gr.getGroupId()+"')\">Click to load!</div></td>");
                pw.println("</tr >");
            }
            pw.println("<script language=\"javascript\" src=\"gwt/gwt.nocache.js\"></script>\n" +
                    "    \n" +
                    "    <!-- Include a history iframe to enable full GWT history support -->\n" +
                    "    <!-- (the id must be exactly as shown)                           -->\n" +
                    "    <iframe src=\"javascript:''\" id=\"__gwt_historyFrame\" style=\"width:0;height:0;border:0\"></iframe>");

        } catch (SQLException e) {
            throw new ServletException(e);
        }
        pw.println("</table></body></html>");
    }
}