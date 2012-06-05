package ro.RestNotificationService.server.database;

import ro.RestNotificationService.server.model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by luc
 * Date: 6/3/12
 * Email: hunealucian@gmail.com
 */
public class MessagesJMSDAO {
    Connection con = null;
    PreparedStatement ptmt = null;
    ResultSet rs = null;

    public MessagesJMSDAO() {
    }

    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = ConnectionFactory.getInstance().getConnection();
        return conn;
    }

    public List findAll(String status) {
        List<Notification> result = new LinkedList<Notification>();
        Notification notif = null;
        try {
            String querystring = "SELECT \n" +
                    " n.IDNOTIFICATIONS as IDNOTIF,\n" +
                    " t.TOPICNAME as DENTOPIC,\n" +
                    " t.idTOPICS as IDTOPIC,\n" +
                    " u.idUSERS as IDUSER,\n" +
                    " u.USERNAME as USERNAME,\n" +
                    " msg.DESCRIERE as MSGDESC,\n" +
                    " msg.idMESSAGES as MSGID\n" +
                    "FROM messages.NOTIFICATIONS n\n" +
                    " LEFT JOIN messages.TOPICS t ON (t.idTOPICS = n.FK_ID_TOPIC)\n" +
                    " LEFT JOIN messages.USERS u ON (u.idUSERS = n.FK_ID_USER AND u.STATUS='online')\n" +
                    " INNER JOIN \n" +
                    "  (\n" +
                    "    select m.DESCRIERE as DESCRIERE, m.idMESSAGES as idMESSAGES,m.FK_ID_TOPIC from messages.MESSAGES m \n" +
                    "        ORDER BY m.LASTMODIFIED LIMIT 2\n" +
                    "  ) msg ON (msg.FK_ID_TOPIC = n.FK_ID_TOPIC)\n" +
                    "WHERE 0=0 ";


            System.out.println(querystring);
            con = getConnection();
            ptmt = con.prepareStatement(querystring);
            rs = ptmt.executeQuery();
            while (rs.next()) {
                notif = new Notification();
                notif.setId(Integer.parseInt(rs.getString("IDNOTIF")));
                notif.setId_user(Integer.parseInt(rs.getString("IDUSER")));
                notif.setUserName(rs.getString("USERNAME"));
                notif.setTopicName(rs.getString("DENTOPIC"));
                notif.setMessageContext(rs.getString("MSGDESC"));
                notif.setId_msg(Integer.parseInt(rs.getString("MSGID")));
                notif.setId_topic(Integer.parseInt(rs.getString("IDTOPIC")));

                result.add(notif);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ptmt != null)
                    ptmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    /**
     * return list of messages to be sent when new message is added to topic
     * @param messageId
     * @return
     */
    public List getListByNewMessage(int messageId) {
        List<Notification> result = new LinkedList<Notification>();
        Notification notif = null;
        try {
            String querystring = "SELECT \n" +
                    "  a.IDNOTIF AS IDNOTIF,\n" +
                    "  a.IDUSER AS IDUSER,\n" +
                    "  a.USERNAME AS USERNAME,\n" +
                    "  t.DENUMIRE AS DENTOPIC,\n" +
                    "  t.idTOPICS AS IDTOPIC,\n" +
                    "  m.idMESSAGES AS MSGID,\n" +
                    "  m.DESCRIERE AS MSGDESC\n" +
                    "FROM messages.MESSAGES m\n" +
                    "  LEFT JOIN messages.TOPICS t ON ( t.idTOPICS = m.FK_ID_TOPIC )\n" +
                    "  INNER JOIN (\n" +
                    "    SELECT n.fk_id_topic AS TOPICID, u.STATUS AS STATUS, n.idNOTIFICATIONS AS IDNOTIF, u.idUSERS AS IDUSER ,\n" +
                    "        u.USERNAME AS USERNAME\n" +
                    "    FROM messages.NOTIFICATIONS n\n" +
                    "      LEFT JOIN messages.USERS u ON ( u.idUSERS = n.FK_ID_USER )\n" +
                    "  ) a ON ( a.TOPICID = t.idTOPICS AND a.STATUS = 'online' )\n" +
                    "WHERE m.idMESSAGES = ?";


            System.out.println(querystring);
            con = getConnection();
            ptmt = con.prepareStatement(querystring);
            ptmt.setString(1, String.valueOf(messageId));
            rs = ptmt.executeQuery();
            while (rs.next()) {
                notif = new Notification();
                notif.setId(Integer.parseInt(rs.getString("IDNOTIF")));
                notif.setId_user(Integer.parseInt(rs.getString("IDUSER")));
                notif.setUserName(rs.getString("USERNAME"));
                notif.setTopicName(rs.getString("DENTOPIC"));
                notif.setMessageContext(rs.getString("MSGDESC"));
                notif.setId_msg(Integer.parseInt(rs.getString("MSGID")));
                notif.setId_topic(Integer.parseInt(rs.getString("IDTOPIC")));

                result.add(notif);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ptmt != null)
                    ptmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    /**
     * return
     * @param userId
     * @return
     */
    public List getListBySubscriberId(int userId){
        List<Notification> result = new LinkedList<Notification>();
        Notification notif = null;
        try {
            String querystring = "SELECT\n" +
                    "  n.IDNOTIFICATIONS AS IDNOTIF,\n" +
                    "  u.idUSERS AS IDUSER,\n" +
                    "  u.USERNAME AS USERNAME,\n" +
                    "  t.DENUMIRE AS DENTOPIC,\n" +
                    "  t.idTOPICS AS IDTOPIC,\n" +
                    "  a.idMESSAGES AS MSGID,\n" +
                    "  a.DESCRIERE AS MSGDESC\n" +
                    "FROM messages.NOTIFICATIONS n\n" +
                    "  LEFT JOIN messages.USERS u ON (u.idUSERS = n.FK_ID_USER)\n" +
                    "  RIGHT JOIN messages.TOPICS t ON ( t.idTOPICS = n.fk_id_topic )\n" +
                    "  INNER JOIN (\n" +
                    "    SELECT m.LASTMODIFIED AS LASTMODIFIED, m.FK_ID_TOPIC AS FK_ID_TOPIC, m.idMESSAGES AS idMESSAGES,\n" +
                    "         m.DESCRIERE AS DESCRIERE\n" +
                    "    FROM messages.MESSAGES m\n" +
                    "  ) a ON ( a.LASTMODIFIED BETWEEN n.LAST_CHECK_DATE AND NOW() AND a.FK_ID_TOPIC = n.FK_ID_TOPIC)\n" +
                    "WHERE u.idUSERS = ?";
            
            System.out.println(querystring);
            con = getConnection();
            ptmt = con.prepareStatement(querystring);
            ptmt.setString(1, String.valueOf(userId));
            rs = ptmt.executeQuery();
            while (rs.next()) {
                notif = new Notification();
                notif.setId(Integer.parseInt(rs.getString("IDNOTIF")));
                notif.setId_user(Integer.parseInt(rs.getString("IDUSER")));
                notif.setUserName(rs.getString("USERNAME"));
                notif.setTopicName(rs.getString("DENTOPIC"));
                notif.setMessageContext(rs.getString("MSGDESC"));
                notif.setId_msg(Integer.parseInt(rs.getString("MSGID")));
                notif.setId_topic(Integer.parseInt(rs.getString("IDTOPIC")));

                result.add(notif);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ptmt != null)
                    ptmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;
    }
    
    public void updateNotificationLastChecked(Notification notif){
        try {
            String querystring ="UPDATE messages.NOTIFICATIONS n SET n.LAST_CHECK_DATE = NOW() WHERE n.idNOTIFICATIONS =?";
            con = getConnection();
            ptmt = con.prepareStatement(querystring);

            ptmt.setString(1, String.valueOf(notif.getId()));
            ptmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (ptmt != null)
                    ptmt.close();
                if (con != null)
                    con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

//    public EmployeeBean findByPrimaryKey(String empId) {
//
//        EmployeeBean employeeBean = null;
//        try {
//            String querystring ="SELECT * FROM EMPLOYEE WHERE EMP_ID =?";
//            con = getConnection();
//            ptmt = con.prepareStatement(querystring);
//            ptmt.setString(1, empId);
//            rs = ptmt.executeQuery();
//            if (rs.next()) {
//                employeeBean = new EmployeeBean();
//                employeeBean.setEmpId(rs.getString(1));
//                employeeBean.setEmpName(rs.getString(2));
//                employeeBean.setEmpAddr(rs.getString(3));
//                employeeBean.setEmpEmail(rs.getString(4));
//                employeeBean.setEmpPhone(rs.getString(5));
//
//
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (rs != null)
//                    rs.close();
//                if (ptmt != null)
//                    ptmt.close();
//                if (con != null)
//                    con.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//        return employeeBean;
//    }
}