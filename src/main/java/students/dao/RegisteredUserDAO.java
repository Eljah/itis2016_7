package students.dao;

import org.apache.log4j.Logger;
import students.logic.ManagementSystem;
import students.logic.RegisteredUser;
import students.logic.Student;
import students.web.RegisterServlet;

import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ilya Evlampiev on 05.10.2015.
 */
public class RegisteredUserDAO {
    static Logger log = Logger.getLogger(RegisteredUserDAO.class);

    public List<RegisteredUser> list() throws SQLException {

        List<RegisteredUser> users = new ArrayList<RegisteredUser>();

        Connection connection = ManagementSystem.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT user_name, user_pass, email FROM users");
        ResultSet resultSet = statement.executeQuery();

        {
            while (resultSet.next()) {
                RegisteredUser user = new RegisteredUser();
                user.setUsername(resultSet.getString("user_name"));
                user.setPasswordHash(resultSet.getString("user_pass"));
                user.setEmail(resultSet.getString("email"));
                users.add(user);
            }
        }
        return users;
    }

    public boolean addUser(RegisteredUser user, String role)
    {
        Connection connection = ManagementSystem.getInstance().getConnection();
        log.trace("Connection established");
        try {
            connection.setAutoCommit(false);

//неправильно сделано - сначла кладем связь
            PreparedStatement stmt2 = null;
            stmt2 = connection.prepareStatement("INSERT INTO user_roles "
                    + "(user_name, role_name)"
                    + "VALUES( ?,  ?)");
            stmt2.setString(1, user.getUsername());
            stmt2.setString(2, role);
            stmt2.execute();
            log.trace("Addition to user_roles");

            //и только затем саму сущность
            PreparedStatement stmt = null;
            stmt = connection.prepareStatement("INSERT INTO users "
                    + "(user_name, user_pass, email)"
                    + "VALUES( ?,  ?,  ?)");
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getEmail());
            stmt.execute();
            log.trace("Addition to users");
            connection.commit();
            stmt.close();
            stmt2.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if(connection!=null) connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                log.error("Rollback failed");
            }
            log.error("Addition on new user failed "+e.getLocalizedMessage());
            return false;
        }
    }

    public boolean updateUser(String oldusername, RegisteredUser user, String new_role, InputStream is)
    {
        Connection connection = ManagementSystem.getInstance().getConnection();
        log.trace("Connection established");
        try {
            connection.setAutoCommit(false);

//неправильно сделано - сначла кладем связь
            PreparedStatement stmt2 = null;
            stmt2 = connection.prepareStatement("UPDATE user_roles SET user_name =  ?, role_name=? " +
                                    "WHERE user_name =  ?");
            stmt2.setString(1, user.getUsername());
            stmt2.setString(2, new_role);
            stmt2.setString(3,oldusername);
            stmt2.execute();
            log.trace("Update to user_roles");

            //и только затем саму сущность
            PreparedStatement stmt = null;
            stmt = connection.prepareStatement("UPDATE users SET user_name =  ?, user_pass=?, email=?, photo=? " +
                                                        "WHERE user_name =  ?");
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getEmail());
            if (is != null) {
                // fetches input stream of the upload file for the blob column
                stmt.setBlob(4, is);
            }
            stmt.setString(5, oldusername);

            stmt.execute();
            log.trace("Update to users");
            connection.commit();
            stmt.close();
            stmt2.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if(connection!=null) connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                log.error("Rollback failed");
            }
            log.error("Update on new user failed "+e.getLocalizedMessage());
            return false;
        }
    }

    public RegisteredUser getUserByName(String username) throws SQLException {
        Connection connection = ManagementSystem.getInstance().getConnection();
        log.trace("Connection established");
        RegisteredUser user = new RegisteredUser();
        PreparedStatement stmt = connection.prepareStatement("SELECT user_name, user_pass, email FROM users WHERE user_name = ?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            user.setUsername(rs.getString("user_name"));
            user.setPasswordHash(rs.getString("user_pass"));
            user.setEmail(rs.getString("email"));
        }
        rs.close();
        stmt.close();
        return user;
    }

    public Blob getBlob(String username) throws SQLException {
        Connection connection = ManagementSystem.getInstance().getConnection();
        log.trace("Connection established");
        //RegisteredUser user = new RegisteredUser();
        Blob photo=null;
        PreparedStatement stmt = connection.prepareStatement("SELECT photo FROM users WHERE user_name = ?");
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            photo=rs.getBlob("photo");
        }
        rs.close();
        stmt.close();
        return photo;
    }

    public void setUUID(String uuid, String username) throws SQLException
    {
        Connection connection = ManagementSystem.getInstance().getConnection();
        log.trace("Connection established");
        try {
            PreparedStatement stmt = null;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(new java.util.Date()); // Now use today date.
            c.add(Calendar.DATE, 30);
            java.util.Date now_plus_30_days=c.getTime();


            stmt = connection.prepareStatement("UPDATE users SET uuid =  ?, deletetoken = ? " +
                    "WHERE user_name =  ?");
            stmt.setString(1, uuid);
            stmt.setDate(2, new Date(now_plus_30_days.getTime()));
            stmt.setString(3, username);
            stmt.execute();
            log.trace("Added UUID to user "+username);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Update on new user failed "+e.getLocalizedMessage());
        }

    }

    public void deleteUUID(String username) throws SQLException
    {
        Connection connection = ManagementSystem.getInstance().getConnection();
        log.trace("Connection established");
        try {
            PreparedStatement stmt = null;
            stmt = connection.prepareStatement("UPDATE users SET uuid =  NULL " +
                    "WHERE user_name =  ?");
            stmt.setString(1, username);
            stmt.execute();
            log.trace("Deleted  UUID to user "+username);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Update on new user failed "+e.getLocalizedMessage());
        }

    }

    public void deleteUUIDolderThanTime(java.util.Date time) throws SQLException
    {
        Connection connection = ManagementSystem.getInstance().getConnection();
        log.trace("Connection established");
        try {
            log.trace("Requested to delete expired UUID at time " +time.toString());
            PreparedStatement stmt = null;
            stmt = connection.prepareStatement("UPDATE users SET uuid =  NULL, deletetoken = NULL " +
                    "WHERE deletetoken <  ?");
            stmt.setDate(1, new Date(time.getTime()));
            int rows=stmt.executeUpdate();
            if (rows>0)  log.info("Succeed to delete expired UUID at time " +time.toString());

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Update on new user failed "+e.getLocalizedMessage());
        }

    }

    public String findUsernameByUUID(String uuid) throws SQLException
    {
        Connection connection = ManagementSystem.getInstance().getConnection();
        log.trace("Connection established");
        String username=null;
        PreparedStatement stmt = connection.prepareStatement("SELECT user_name FROM users WHERE uuid = ?");
        stmt.setString(1, uuid);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            username=rs.getString("user_name");
        }
        rs.close();
        stmt.close();
        return username;
    }

    public boolean checkUserExists(String username) throws SQLException
    {
        String returnedUsername=null;
        Connection connection = ManagementSystem.getInstance().getConnection();

        log.trace("Connection established");
        //PreparedStatement stmt = connection.prepareStatement("SELECT user_name FROM users WHERE user_name = ?");
        //stmt.setString(1, username);
        //ResultSet rs = stmt.executeQuery();
       Statement stmt = connection.createStatement();
        log.info("SELECT user_name FROM users WHERE user_name = "+username);
        ResultSet rs = stmt.executeQuery("SELECT user_name FROM users WHERE user_name = "+username);
        while (rs.next()) {
            returnedUsername=rs.getString("user_name");
            log.info("Something was fount there");

        }
        rs.close();
        stmt.close();
        return (returnedUsername==null);
    }
}
