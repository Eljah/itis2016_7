package students.logic;

/**
 * Created by Ilya Evlampiev on 06.10.2015.
 */
public class Note {
    private RegisteredUser user;
    private String text;

    public RegisteredUser getUser()
    {
        return this.user;
    }

    public String getText()
    {
        return this.text;
    }

    public void setUser(RegisteredUser user)
    {
        this.user=user;
    }

    public void setText(String text)
    {
        this.text=text;
    }


}
