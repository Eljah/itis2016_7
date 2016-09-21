package students.quartz;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import students.dao.RegisteredUserDAO;
import students.logic.RegisteredUser;

import java.sql.Date;
import java.sql.SQLException;

/**
 * Created by Ilya Evlampiev on 12.10.2015.
 */
public class DeleterUUIDJob implements Job {
    static Logger log = Logger.getLogger(DeleterUUIDJob.class);
    RegisteredUserDAO userDAO=new RegisteredUserDAO();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
       log.info("quartz is called");
        try {
            java.util.Date newDate=new java.util.Date();
            log.trace("UUIDs are deleted for time "+newDate.toString());
            userDAO.deleteUUIDolderThanTime(newDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
