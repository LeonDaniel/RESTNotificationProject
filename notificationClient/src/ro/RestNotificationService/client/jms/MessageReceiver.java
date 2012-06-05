package ro.RestNotificationService.client.jms;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * Date: 6/2/12
 * @author Daniel Leon
 */

public class MessageReceiver extends HttpServlet {
    private Connection connection;
    private String destName = "jms/TopicResource";
    private String connectionName = "jms/TopicConnectionFactoryServer";
    private Session session = null;
    private Destination dest = null;
    private MessageConsumer consumer = null;
    private TextMessage message = null;
    private ConnectionFactory connectionFactory = null;

    /////////////////////////////////////////
    Context jndiContext = null;
    TopicConnectionFactory topicConnectionFactory = null;
    TopicConnection topicConnection = null;
    TopicSession topicSession = null;
    Topic topic = null;
    TopicSubscriber topicSubscriber = null;
    JMSTopicMapListener topicListener = null;

    @Override
    public void init() throws ServletException {
        super.init();
        /*
         * Set the Context Object.
         * Lookup the Topic Connection Factory.
         * Lookup the JMS Destination.
         */
        try {
            jndiContext = new InitialContext();
            topicConnectionFactory =
                    (TopicConnectionFactory)jndiContext.lookup(connectionName);
            topic = (Topic)jndiContext.lookup(destName);
        } catch (NamingException e) {
            System.out.println("Lookup failed: " + e.toString());
            System.exit(1);
        }
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        /*
        * Create connection.
        * Create session from connection; false means session is
        * not transacted.
        * Create subscriber.
        * Register message listener (TextListener).
        * Receive text messages from topic.
        * Close connection.
        */
        try {
            topicConnection = topicConnectionFactory.createTopicConnection();
            topicSession =
                    topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            topicSubscriber = topicSession.createSubscriber(topic);
            topicListener = new JMSTopicMapListener();
            topicSubscriber.setMessageListener(topicListener);
            topicConnection.start();
            System.out.println("Subscripted to topic: \"" + destName + "\"");
        } catch (JMSException e) {
            System.out.println("Exception occurred: " + e.toString());
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
