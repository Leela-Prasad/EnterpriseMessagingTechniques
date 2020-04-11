package oreilly.video.messaging.browser;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Enumeration;

public class JMSBrowser {

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("TRADE.Q");

        QueueBrowser browser = session.createBrowser(queue);
        Enumeration enumeration = browser.getEnumeration();

        int msgCount = 0;
        while(enumeration.hasMoreElements()) {
            System.out.println(((TextMessage)enumeration.nextElement()).getText());
            //enumeration.nextElement();
            ++msgCount;
        }
        System.out.println("There are " + msgCount + " messages in the queue");

        // If we want to browse messages again then we need createBrowser again, so we need to do browser.close
        // before creating Browser
        // connection.close will close session, browser automatically.
        browser.close();
        connection.close();
    }
}
