package oreilly.video.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSSender {

    public static void main(String[] args) throws JMSException, InterruptedException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        // Here since it is in transaction Ack mode is ignored.
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("TRADE.Q");

        TextMessage message1 = session.createTextMessage();
        message1.setText("Buy APPL 1000 Shares");

        TextMessage message2 = session.createTextMessage();
        message2.setText("Buy IBM 500 Shares");

        MessageProducer sender = session.createProducer(queue);
        sender.send(message1);
        System.out.println("Message1 Sent");

        // Thread.sleep(3000);

        /* if(true) {
            connection.close();
            throw new RuntimeException("oops!!");
        }*/

        sender.send(message2);
        System.out.println("Message2 Sent");

        session.commit();
        connection.close();

    }
}
