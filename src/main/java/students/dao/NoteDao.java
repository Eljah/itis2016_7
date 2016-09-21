package students.dao;

import org.apache.log4j.Logger;
import students.logic.ManagementSystem;
import students.logic.Note;
import students.logic.RegisteredUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ilya Evlampiev on 06.10.2015.
 */
public class NoteDao {
    static Logger log = Logger.getLogger(RegisteredUserDAO.class);

    public boolean addNote(Note note)
    {
        Connection connection = ManagementSystem.getInstance().getConnection();
        log.trace("Connection established");
        try {
            PreparedStatement stmt2 = null;
            stmt2 = connection.prepareStatement("INSERT INTO notes "
                    + "(user, text)"
                    + "VALUES( ?,  ?)");
            stmt2.setString(1, note.getUser().getUsername());
            stmt2.setString(2, note.getText());
            stmt2.execute();
            log.trace("Addition to notes by user " + note.getUser().getUsername());
            stmt2.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Addition of new comment failed "+e.getLocalizedMessage());
            return false;
        }
    }

    public List<Note> getNotes() throws SQLException {
        List<Note> notes = new ArrayList<Note>();

        Connection connection = ManagementSystem.getInstance().getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT user, text FROM notes");
        ResultSet resultSet = statement.executeQuery();

        {
            while (resultSet.next()) {
                Note note = new Note();
                RegisteredUser user=new RegisteredUser();
                user.setUsername(resultSet.getString("user"));
                note.setUser(user);
                note.setText(resultSet.getString("text"));
                notes.add(note);
            }
        }
        return notes;
    }

}
