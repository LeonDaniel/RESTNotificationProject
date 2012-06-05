package ro.RestNotificationService.server.jms.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by luc
 * Date: 6/5/12
 * Email: hunealucian@gmail.com
 */
public class NewMessageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        //id of new Message to send notification
        int msgId = Integer.getInteger(httpServletRequest.getParameter("idMessage"));

        System.out.println("dsadsadsa");
    }
}
