package ro.RestNotificationService.server.jms.servlets;

import ro.RestNotificationService.server.database.MessagesJMSDAO;
import ro.RestNotificationService.server.jms.JMSUtils;
import ro.RestNotificationService.server.model.Notification;

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
public class SubscribeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        //userId to send notifications
        int userId = Integer.parseInt(httpServletRequest.getParameter("idUser"));

        List<Notification> list = new LinkedList<Notification>();

        MessagesJMSDAO dao = new MessagesJMSDAO();
        list = dao.getListBySubscriberId(userId);

        boolean succes;
        if( list.size() > 0 ){
            succes = JMSUtils.sendMessages(list);

            //updatam cand a fost vizualizata ultima oara un topic de catre un user
            for (Notification notification : list) {
                dao.updateNotificationLastChecked(notification);
            }
        }
    }
}
