package oreilly.video.messaging.client.ack;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSReceiver {

    public static void main(String[] args) throws JMSException, InterruptedException {

        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue("TRADE.Q");

        MessageConsumer consumer = session.createConsumer(queue);
        TextMessage message = (TextMessage) consumer.receive();
        Thread.sleep(5000);
        //if(true) {
        //    connection.close();
        //    throw new RuntimeException("Oops!!!");
        //}
        System.out.println(message.getText());
        message.acknowledge();

        connection.close();
    }
}
