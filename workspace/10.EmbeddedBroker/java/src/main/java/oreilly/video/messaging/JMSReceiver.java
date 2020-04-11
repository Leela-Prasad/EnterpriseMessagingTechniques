package oreilly.video.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSReceiver {

    public static void main(String[] args) throws JMSException {

        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61888");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("TRADE.Q");

        MessageConsumer consumer = session.createConsumer(queue);
        System.out.println("Waiting for Messages ...");
        TextMessage message = (TextMessage) consumer.receive();
        System.out.println(message.getText());

        connection.close();
    }
}
