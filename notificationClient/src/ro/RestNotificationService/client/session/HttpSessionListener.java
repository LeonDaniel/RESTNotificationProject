package ro.RestNotificationService.client.session;

import ro.RestNotificationService.client.database.UserDAO;
import ro.RestNotificationService.client.model.User;

import javax.servlet.http.HttpSessionEvent;

/**
 * Created by luc
 * Date: 6/4/12
 * Email: hunealucian@gmail.com
 */
public class HttpSessionListener implements javax.servlet.http.HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        //get user from session
        User user = (User)httpSessionEvent.getSession().getAttribute("user");

        //update user status to 'offline'
        user.setStatus("offline");

        UserDAO userDAO = new UserDAO();
        userDAO.updateStatus(user);
    }
}
