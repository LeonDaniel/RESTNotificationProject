package ro.jms.utils;

import ro.jms.model.Notification;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by luc
 * Date: 6/3/12
 * Email: hunealucian@gmail.com
 */
public class JMSUtils {

    /**
     * Initialize the context object
     * @param jndiContext
     */
    public static Context setUpJNDIContext(Context jndiContext){
        try{
            jndiContext = new InitialContext();
        } catch (NamingException ex){
            System.out.println("Could not create JNDI API context: " + ex.toString());
        }
        
        return jndiContext;
    }
    

    public static MapMessage createMessage(MapMessage message, Notification notification) throws JMSException{
        message.setStringProperty("from", "from@" + "JMS Server");
        message.setStringProperty("to", "to@" + notification.getUserName());
        message.setStringProperty("subject", "Topic Message");
        message.setStringProperty("content",notification.getMessageContext());
        message.setStringProperty("topic",notification.getTopicName());
        message.setStringProperty("resourceInfo",notification.getResourceInfo());

        
        return message;
    }
}
