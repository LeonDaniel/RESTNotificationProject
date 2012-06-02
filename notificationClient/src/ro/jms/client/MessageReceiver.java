package ro.jms.client;

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
    private Session session = null;
    private Destination dest = null;
    private MessageConsumer consumer = null;
    private TextMessage message = null;
    private ConnectionFactory connectionFactory = null;

    @Override
    public void init() throws ServletException {
        super.init();

        String destName = "jms/TopicResource";
        Context jndiContext = null;

        connection = null;


        System.out.println("Destination name is " + destName);

        try {
            jndiContext = new InitialContext();
        } catch (NamingException e) {
            System.out.println("Could not create JNDI API context: " + e.toString());
            System.exit(1);
        }

        try {
            connectionFactory = (ConnectionFactory) jndiContext.lookup("jms/TopicConnectionFactoryServer");
            dest = (Destination) jndiContext.lookup(destName);
        } catch (Exception e) {
            System.out.println("JNDI API lookup failed: " + e.toString());
            System.exit(1);
        }
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createConsumer(dest);
            connection.start();
            while (true) {
                Message m = consumer.receive(1);
                if (m != null) {
                    if (m instanceof TextMessage) {
                        message = (TextMessage) m;
                        System.out.println("Reading message: " + message.getText());
                    } else {
                        break;
                    }
                }
            }
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
