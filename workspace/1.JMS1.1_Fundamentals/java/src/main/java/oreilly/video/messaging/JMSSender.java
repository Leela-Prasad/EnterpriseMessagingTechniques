package oreilly.video.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSSender {

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("TRADE.Q");

        TextMessage message = session.createTextMessage();
        message.setText("Buy APPL 1000 Shares");
        message.setStringProperty("TraderName", "Leela");

        MessageProducer sender = session.createProducer(queue);
        // sender.setPriority(3);
        sender.send(message);
        System.out.println("Message Sent");

        // Here Session will also be closed as part of connection close
        connection.close();

    }
}
