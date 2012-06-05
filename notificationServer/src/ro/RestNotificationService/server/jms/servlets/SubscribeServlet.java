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
public class SubscribeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        //userId to send notifications
        int userId = Integer.getInteger(httpServletRequest.getParameter("idUser"));
    }
}
