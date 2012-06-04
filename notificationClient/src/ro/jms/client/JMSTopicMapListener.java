package ro.jms.client;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by luc
 * Date: 6/3/12
 * Email: hunealucian@gmail.com
 */
public class JMSTopicMapListener implements MessageListener {

    public void onMessage(Message message) {
        MapMessage msg = null;

        try {
            if (message instanceof MapMessage) {
                msg = (MapMessage)message;
                String from = msg.getStringProperty("from");
                String to = msg.getStringProperty("to"); 
                String userID = msg.getString("toID");
                String subject = msg.getStringProperty("subject");
                String content = msg.getStringProperty("content");
                String msgID = msg.getStringProperty("contentID");
                String topicName = msg.getStringProperty("topic");
                String topicID = msg.getStringProperty("topicID");
                
                System.out.println("READING MESSAGE \n=============== \nFrom: " +
                        from + "\nTo: " + to + "\nSubject: " +
                        subject +
                        "\nTopic: " +
                        topicName +
                        "\nContent: " + content);
            } else {
                System.out.println("Wrong type");
            }
        } catch (JMSException e) {
            System.out.println("JMSException in onMessage(): " + e.toString());
        } catch (Throwable t) {
            System.out.println("Exception in onMessage():" + t.getMessage());
        }
    }
}