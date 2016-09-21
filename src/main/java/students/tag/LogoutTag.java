package students.tag;

        import javax.servlet.jsp.JspException;
        import javax.servlet.jsp.tagext.TagSupport;
        import java.io.IOException;

public class LogoutTag extends TagSupport{
    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        try {
            pageContext.getOut().print( "<div style=\"text-align: right\"><a href=\"edituser\">Мои данные</a> <a href=\"logout.jsp\">Выйти</a></div>" );
        } catch(IOException ioException) {
            throw new JspException("Error: " + ioException.getMessage());
        }
        return SKIP_BODY;
    }
}