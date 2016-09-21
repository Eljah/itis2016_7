package students.locales;

import com.google.gwt.dev.ModuleTabPanel;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/locales")
public class LocalesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("accept-language header: <em>" + request.getHeader("accept-language") + "</em>");

        out.println("<hr/>");

        Locale locale = request.getLocale();
        out.println("Preferred Locale:<br/>");
        out.println(displayLocaleInfo(locale));

        out.println("<hr/>");

        ResourceBundle bundle = ResourceBundle.getBundle("i18n.TestBundle", locale);
        out.println("Question: " + bundle.getString("my.question"));

        out.println("<hr/>");

        out.println("Acceptable Locales:<br/>");
        Enumeration<?> locales = request.getLocales();
        while (locales.hasMoreElements()) {
            Locale loc = (Locale) locales.nextElement();
            out.println(displayLocaleInfo(loc));
        }
        Object lang = request.getSession().getAttribute("language");
        if (lang != null) {
            Locale sessionLocale = new Locale(lang.toString());
            out.println("Session Locale:<br/>");
            out.println(displayLocaleInfo(sessionLocale));
            ResourceBundle bundle2 = ResourceBundle.getBundle("i18n.TestBundle", sessionLocale);
            out.println("Question: " + bundle2.getString("my.question"));

        }

    }

    private String displayLocaleInfo(Locale locale) {
        StringBuffer sb = new StringBuffer();
        sb.append("Locale: <em>" + locale.toString());
        sb.append("</em>, Language: <em>" + locale.getLanguage());
        sb.append("</em>, Country: <em>" + locale.getCountry());
        sb.append("</em>, Display name: <em>" + locale.getDisplayName());
        sb.append("</em><br/>");
        return sb.toString();
    }

}