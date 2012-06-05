package ro.RestNotificationService.server.database;

import ro.RestNotificationService.server.model.Notification;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by luc
 * Date: 6/4/12
 * Email: hunealucian@gmail.com
 */
public class NotificationsUtils {
    
    public static List<Notification> getTopicMsgToSend(){
        //lista de mesaje ce trebuiesc puse in topic
        List<Notification> result = new LinkedList<Notification>();
        
        String status = "pending";

        MessagesJMSDAO dao = new MessagesJMSDAO();
        result = dao.findAll(status);
        
        return result;
    }
}
