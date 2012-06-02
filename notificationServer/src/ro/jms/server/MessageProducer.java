package ro.jms.server;


import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.Override;

/**
 *
 * Date: 6/2/12
 * @author Daniel Leon
 * @author Lucian Hunea
 */

public class MessageProducer extends HttpServlet {
    private Context jndiContext;
    private ConnectionFactory connectionFactory;
    private String resourceJNDIName = "jms/TopicResource";
    private String connectionFactoryJNDIName = "jms/TopicConnectionFactoryServer";
    private Destination dest;

    @Override
    public void init() throws ServletException {
        super.init();
        jndiContext = null;

        try {
            jndiContext = new InitialContext();
        } catch (NamingException e) {
            System.out.println("Could not create JNDI API context: " + e.toString());
            System.exit(1);
        }

        connectionFactory = null;
        dest = null;

        try {
            connectionFactory = (ConnectionFactory) jndiContext.lookup(connectionFactoryJNDIName);
            dest = (Destination) jndiContext.lookup(resourceJNDIName);
        } catch (Exception e) {
            System.out.println("JNDI API lookup failed: " + e.toString());
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        Connection connection = null;
        javax.jms.MessageProducer producer = null;
        final int NUM_MSGS = 5;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(dest);
            TextMessage message = session.createTextMessage();

            for (int i = 0; i < NUM_MSGS; i++) {
                message.setText("This is message from Testing DEMO " + (i + 2));
                System.out.println("Sending message: " + message.getText());
                producer.send(message);
            }

            producer.send(session.createMessage());
        } catch (JMSException e) {
            System.out.println("Exception occurred: " + e.toString());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                }
            }
        }
    }
}
