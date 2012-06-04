package ro.jms.client.listeners;

import ro.jms.client.dbUtils.UserDAO;
import ro.jms.client.model.User;

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
