package ro.jms.utils;

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
    

    public static MapMessage createMessage(MapMessage message, String from, String to, String topicName) throws JMSException{
        message.setStringProperty("from", "from@" + from);
        message.setStringProperty("to", "to@" + to);
        message.setStringProperty("subject",
                "Topic Message ");
        message.setStringProperty("content",
                "Message - " + " in Topic: \"" +
                        topicName + "\"");
        System.out.println("Publishing message: " +
                message.getStringProperty("from"));
        System.out.println("Publishing message: " +
                message.getStringProperty("to"));
        System.out.println("Publishing message: " +
                message.getStringProperty("subject"));
        System.out.println("Publishing message: " +
                message.getStringProperty("content"));
        
        return message;
    }
}
