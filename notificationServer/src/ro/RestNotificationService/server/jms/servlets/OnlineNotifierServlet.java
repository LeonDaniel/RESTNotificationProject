package ro.RestNotificationService.server.jms.servlets;

import ro.RestNotificationService.server.database.UserDAO;
import ro.RestNotificationService.server.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by luc
 * Date: 6/5/12
 * Email: hunealucian@gmail.com
 */
public class OnlineNotifierServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        //get userID
        int userId = Integer.parseInt(httpServletRequest.getParameter("idUser"));
        String status = httpServletRequest.getParameter("state");

        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        
        //change status to online by userID
        UserDAO dao = new UserDAO();
        dao.updateStatus(user);
    }
}
