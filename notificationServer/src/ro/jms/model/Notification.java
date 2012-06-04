package ro.jms.model;

/**
 * Created by luc
 * Date: 6/4/12
 * Email: hunealucian@gmail.com
 */
public class Notification {
    private int id = -1;
    private int fk_id_user;
    private String userName;
    private String resourceInfo;
    private String topicName;
    private String messageContext;
    private String status;

    public Notification() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFk_id_user() {
        return fk_id_user;
    }

    public void setFk_id_user(int fk_id_user) {
        this.fk_id_user = fk_id_user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getResourceInfo() {
        return resourceInfo;
    }

    public void setResourceInfo(String resourceInfo) {
        this.resourceInfo = resourceInfo;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getMessageContext() {
        return messageContext;
    }

    public void setMessageContext(String messageContext) {
        this.messageContext = messageContext;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
