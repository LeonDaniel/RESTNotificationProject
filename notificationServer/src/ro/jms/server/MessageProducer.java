package ro.jms.server;


import ro.jms.model.Notification;
import ro.jms.utils.ConnParams;
import ro.jms.utils.JMSUtils;
import ro.jms.utils.dbAcces.NotificationsUtils;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.Override;
import java.util.List;

/**
 * Date: 6/2/12
 *
 * @author Daniel Leon
 * @author Lucian Hunea
 */

public class MessageProducer extends HttpServlet {
    Context jndiContext = null;                             // jndi context
    TopicConnection topicConnection = null;                 // topic connection ?
    TopicConnectionFactory topicConnectionFactory = null;   // connection factory
    TopicSession topicSession = null;                       // topic session
    Topic topic = null;                                     // za topic
    TopicPublisher topicPublisher = null;                   // topic publisher
    MapMessage message = null;

    @Override
    public void init() throws ServletException {
        jndiContext = JMSUtils.setUpJNDIContext(jndiContext);

        /*
        * Lookup the Topic Connection Factory.
        * Lookup the JMS Destination.
        */
        try {
            topicConnectionFactory = (TopicConnectionFactory) jndiContext.lookup(ConnParams.connectionFactoryJNDIName);
            topic = (Topic) jndiContext.lookup(ConnParams.resourceJNDIName);
        } catch (NamingException e) {
            System.out.println("Lookup failed: " + e.toString());
            System.exit(1);
        }

    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            topicConnection = topicConnectionFactory.createTopicConnection();
            topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            topicPublisher = topicSession.createPublisher(topic);

            try {
                List<Notification> list = NotificationsUtils.getTopicMsgToSend();

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
        } finally {
            if (topicConnection != null) {
                try {
                    topicConnection.close();
                } catch (JMSException e) {
                    System.out.println("Closing error: " + e.toString());
                }
            }
        }


    }
}
