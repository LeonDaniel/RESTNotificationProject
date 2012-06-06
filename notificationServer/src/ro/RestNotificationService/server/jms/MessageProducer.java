package ro.RestNotificationService.server.jms;


import ro.RestNotificationService.server.model.Notification;
import ro.RestNotificationService.server.database.NotificationsUtils;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.Override;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 6/2/12
 *
 * @author Daniel Leon
 * @author Lucian Hunea
 */

public class MessageProducer extends HttpServlet {

    @Override
    public void init() throws ServletException {
        JMSUtils.setUpJNDIContext();

        /*
        * Lookup the Topic Connection Factory.
        * Lookup the JMS Destination.
        */
        try {
            JMSUtils.topicConnectionFactory= (TopicConnectionFactory) JMSUtils.jndiContext.lookup(ConnParams.connectionFactoryJNDIName);
            JMSUtils.topic = (Topic) JMSUtils.jndiContext.lookup(ConnParams.resourceJNDIName);
        } catch (NamingException e) {
            System.out.println("Lookup failed: " + e.toString());
            System.exit(1);
        }
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        List<Notification> list = new LinkedList<Notification>();
        Notification n = new Notification();
        n.setId(1);
        n.setId_msg(1);
        n.setId_topic(1);
        n.setId_user(1);
        n.setTopicName("Topic bun");
        n.setUserName("Daniel");
        n.setMessageContext("/mesaj");
        n.setStatus("pending");

        list.add(n);
        boolean succes = JMSUtils.sendMessages(list);
    }
}
