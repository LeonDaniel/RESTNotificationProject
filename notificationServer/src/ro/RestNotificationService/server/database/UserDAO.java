package ro.RestNotificationService.server.database;

import ro.RestNotificationService.server.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by luc
 * Date: 6/3/12
 * Email: hunealucian@gmail.com
 */
public class UserDAO {
    Connection con = null;
    PreparedStatement ptmt = null;
    ResultSet rs = null;

    public UserDAO() {
    }

    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = ConnectionFactory.getInstance().getConnection();
        return conn;
    }

    /**
     * Updates a record from db ( user -> status )
     * @param userBean
     */
    public void updateStatus(User userBean) {
        try {
            String querystring ="UPDATE messages.USERS SET STATUS =? WHERE idUSERS =?";
            con = getConnection();
            ptmt = con.prepareStatement(querystring);

            ptmt.setString(1, userBean.getStatus());
            ptmt.setString(2, String.valueOf(userBean.getId()));
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

    /**
     * Delete a record from USERS table by ID
     * @param userID
     */
    public void delete(String userID) {

        try {
            String querystring ="DELETE FROM messages.USERS WHERE idUSERS =?";
            con = getConnection();
            ptmt = con.prepareStatement(querystring);
            ptmt.setString(1, userID);
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
        return null;
    }

    /**
     * Identify in db an user by username and password
     * @param username
     * @param pass
     * @return  User object
     */
    public User findByUsernameAndPass(String username, String pass) {

        User employeeBean = null;
        try {
            String querystring ="SELECT * FROM messages.USERS u WHERE u.USERNAME =? AND u.PASSWORD = ?";
            con = getConnection();
            ptmt = con.prepareStatement(querystring);
            ptmt.setString(1, username);
            ptmt.setString(2, pass);
            rs = ptmt.executeQuery();
            if (rs.next()) {
                employeeBean = new User();
                employeeBean.setId(Integer.parseInt(rs.getString(1)));
                employeeBean.setUsername(rs.getString(2));
                employeeBean.setPassword(rs.getString(3));
                employeeBean.setStatus(rs.getString(4));
                employeeBean.setRole(rs.getString(5));
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
        return employeeBean;
    }
}