package oreilly.video.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSSender {

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61888");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("TRADE.Q");

        TextMessage message = session.createTextMessage();
        message.setText("Buy APPL 1000 Shares");

        MessageProducer sender = session.createProducer(queue);
        sender.send(message);
        System.out.println("Message Sent");

        connection.close();
    }
}
