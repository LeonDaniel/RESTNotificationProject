package ro.jms.utils.dbUtils;

import ro.jms.model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

//    public void add(EmployeeBean employeeBean) {
//        try {
//            String querystring ="INSERT INTO EMPLOYEE VALUES ( ?,?,?,?,?)";
//            con = getConnection();
//            ptmt = con.prepareStatement(querystring);
//            ptmt.setString(1, String.valueOf(System.currentTimeMillis()));
//            ptmt.setString(2, employeeBean.getEmpName());
//            ptmt.setString(3, employeeBean.getEmpAddr());
//            ptmt.setString(4, employeeBean.getEmpEmail());
//            ptmt.setString(5, employeeBean.getEmpPhone());
//            ptmt.executeUpdate();
//
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
//
//    }

//    public void update(EmployeeBean employeeBean) {
//
//        try {
//            String querystring ="UPDATE EMPLOYEE SET EMP_NAME =?,EMP_ADDR =?,"+
//            "EMP_EMAIL =?,EMP_PHONE =?WHERE EMP_ID =?";
//            con = getConnection();
//            ptmt = con.prepareStatement(querystring);
//
//            ptmt.setString(1, employeeBean.getEmpName());
//            ptmt.setString(2, employeeBean.getEmpAddr());
//            ptmt.setString(3, employeeBean.getEmpEmail());
//            ptmt.setString(4, employeeBean.getEmpPhone());
//            ptmt.setString(5, employeeBean.getEmpId());
//            ptmt.executeUpdate();
//
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
//
//    }

    public void delete(String employeeId) {

        try {
            String querystring ="DELETE FROM EMPLOYEE WHERE EMPID =?";
            con = getConnection();
            ptmt = con.prepareStatement(querystring);
            ptmt.setString(1, employeeId);
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

    public List findAll(String status) {
        List<Notification> result = new LinkedList<Notification>();
        Notification notif = null;
        try {
            String querystring ="SELECT \n" +
                    " n.IDNOTIFICATIONS as IDNOTIF,\n" +
                    " t.DENUMIRE as DENTOPIC,\n" +
                    " u.idUSERS as IDUSER,\n" +
                    " u.USERNAME as USERNAME,\n" +
                    " msg.DESCRIERE as MSGDESC,\n" +
                    " n.RESOURCE_INFO as RESOURCE_INFO\n" +
                    "FROM messages.NOTIFICATIONS n\n" +
                    " LEFT JOIN messages.TOPICS t ON (t.idTOPICS = n.FK_ID_TOPIC)\n" +
                    " LEFT JOIN messages.USERS u ON (u.idUSERS = n.FK_ID_USER)\n" +
                    " INNER JOIN \n" +
                    "  (\n" +
                    "    select m.DESCRIERE as DESCRIERE, m.FK_ID_TOPIC as FK_ID_TOPIC from messages.MESSAGES m \n" +
                    "        ORDER BY m.LASTMODIFIED LIMIT 2\n" +
                    "  ) msg ON (msg.FK_ID_TOPIC = n.FK_ID_TOPIC)\n" +
                    "WHERE 0=0 ";

            if( status.length() > 0 ){
                querystring.replace("{0}", "AND m.STATUS='pending'");
            } else {
                querystring.replace("{0}", " ");
            }

            con = getConnection();
            ptmt = con.prepareStatement(querystring);
            rs = ptmt.executeQuery();
            while (rs.next()) {
                notif = new Notification();
                notif.setId(Integer.parseInt(rs.getString("IDNOTIF")));
                notif.setFk_id_user(Integer.parseInt(rs.getString("IDUSER")));
                notif.setUserName(rs.getString("USERNAME"));
                notif.setTopicName(rs.getString("DENTOPIC"));
                notif.setMessageContext(rs.getString("MSGDESC"));
                notif.setResourceInfo(rs.getString("RESOURCE_INFO"));

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