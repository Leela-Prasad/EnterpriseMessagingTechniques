package oreilly.video.messaging.metadata;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Enumeration;

public class JMSMetadata {

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();

        ConnectionMetaData metaData = connection.getMetaData();

        System.out.println("JMS Version: " + metaData.getJMSVersion());
        System.out.println("JMS Provider: " + metaData.getJMSProviderName());
        System.out.println("JMS Provider Version: " + metaData.getProviderVersion());
        System.out.println("JMSX Properties Supported ");
        Enumeration e = metaData.getJMSXPropertyNames();
        while(e.hasMoreElements()) {
            System.out.println(e.nextElement());
        }

        connection.close();
    }
}
