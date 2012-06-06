package ro.RestNotificationService.server.jms;

import ro.RestNotificationService.server.database.NotificationsUtils;
import ro.RestNotificationService.server.model.Notification;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;

/**
 * Created by luc
 * Date: 6/3/12
 * Email: hunealucian@gmail.com
 */
public class JMSUtils {
    public static Context jndiContext = null;                             // jndi context
    public static TopicConnectionFactory topicConnectionFactory = null;   // connection factory
    public static Topic topic = null;                                     // za topic   


    /**
     * Initialize the context object
     * @param
     */
    public static void setUpJNDIContext(){
        try{
            JMSUtils.jndiContext = new InitialContext();
        } catch (NamingException ex){
            System.out.println("Could not create JNDI API context: " + ex.toString());
        }
    }
    

    public static MapMessage createMessage(MapMessage message, Notification notification) throws JMSException{
        message.setStringProperty("from", "from@" + "JMS Server");
        message.setStringProperty("to", notification.getUserName());
        message.setStringProperty("toID", String.valueOf(notification.getId_user()));
        message.setStringProperty("subject", "Topic Message");
        message.setStringProperty("content",notification.getMessageContext());
        message.setStringProperty("contentID", String.valueOf(notification.getId_msg()));
        message.setStringProperty("topic",notification.getTopicName());
        message.setStringProperty("topicID", String.valueOf(notification.getId_topic()));

        System.out.println("Message sent to - " + notification.getUserName() + "//// on topic - " + notification.getTopicName());

        return message;
    }
    
    public static boolean sendMessages(List<Notification> list){
        TopicSession topicSession = null;                       // topic session
        TopicPublisher topicPublisher = null;                   // topic publisher
        TopicConnection topicConnection = null;                 // topic connection ?
        MapMessage message = null;

        try {
            topicConnection = JMSUtils.topicConnectionFactory.createTopicConnection();
            topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            topicPublisher = topicSession.createPublisher(topic);

            try {
//                List<Notification> list = NotificationsUtils.getTopicMsgToSend();

                if (list != null || list.size() > 0) {
                    for (Notification notification : list) {
                        message = topicSession.createMapMessage();
                        message = JMSUtils.createMessage(message, notification);
                        //publish the message in topic
                        topicPublisher.publish(message);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

        } catch (JMSException ex) {
            System.out.println("Exception occurred: " + ex.toString());
            return false;
        } finally {
            if (topicConnection != null) {
                try {
                    topicConnection.close();
                } catch (JMSException e) {
                    System.out.println("Closing error: " + e.toString());
                }
            }
        }

        return true;
    }   
}
