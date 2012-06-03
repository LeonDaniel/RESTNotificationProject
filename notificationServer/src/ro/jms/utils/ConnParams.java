package ro.jms.utils;

/**
 * Created by luc
 * Date: 6/3/12
 * Email: hunealucian@gmail.com
 */
public interface ConnParams {
    //todo de incarcat datele de conectare la jms din fisier.properties

    String resourceJNDIName = "jms/TopicResource";
    String connectionFactoryJNDIName = "jms/TopicConnectionFactoryServer";
    final String jmsProviderHost = "sracanov-au.au.oracle.com";
}
