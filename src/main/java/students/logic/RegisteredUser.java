package students.logic;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ilya Evlampiev on 05.10.2015.
 */
public class RegisteredUser implements java.io.Serializable {
    private String username;
    private String passwordHash;
    private String email;

    public String getUsername()
    {
        return this.username;
    }

    public String getPasswordHash()
    {
        return this.passwordHash;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setUsername(String username)
    {
        this.username=username;
    }

    public void setPassword(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++)
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        this.passwordHash=sb.toString();
    }

    public void setPasswordHash(String passwordHash)
    {
        this.passwordHash=passwordHash;
    }

    public void setEmail(String email)
    {
        this.email=email;
    }


}
