package ro.RestNotificationService.server.jms.servlets;

import ro.RestNotificationService.server.database.MessagesJMSDAO;
import ro.RestNotificationService.server.database.UserDAO;
import ro.RestNotificationService.server.jms.JMSUtils;
import ro.RestNotificationService.server.model.Notification;
import ro.RestNotificationService.server.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by luc
 * Date: 6/5/12
 * Email: hunealucian@gmail.com
 */
public class OnlineNotifierServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        doPost(httpServletRequest, httpServletResponse);
    }

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

        if (status.equals("online")) {
            //trimitem ultimele mesaje in topic spre client
            List<Notification> list = new LinkedList<Notification>();

            MessagesJMSDAO msgDao = new MessagesJMSDAO();
            list = msgDao.getListBySubscriberId(userId);

            boolean succes;
            if (list.size() > 0) {
                succes = JMSUtils.sendMessages(list);

                //updatam cand a fost vizualizata ultima oara un topic de catre un user
                for (Notification notification : list) {
                    msgDao.updateNotificationLastChecked(notification);
                }
            }
        }
    }
}
