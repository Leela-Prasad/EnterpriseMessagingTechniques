package oreilly.video.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSReceiver {

    public static void main(String[] args) throws JMSException, InterruptedException {

        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("TRADE.Q");

        MessageConsumer consumer = session.createConsumer(queue);
        TextMessage message1 = (TextMessage) consumer.receive();
        System.out.println(message1.getText());

        Thread.sleep(3000);

        //if(true) {
        //    connection.close();
        //    throw new RuntimeException("oops");
        //}
        TextMessage message2 = (TextMessage) consumer.receive();
        System.out.println(message2.getText());

        session.commit();

        connection.close();
    }
}
