package oreilly.video.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSReceiver {

    public static void main(String[] args) throws JMSException {

        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("TRADE.Q");

        MessageConsumer consumer = session.createConsumer(queue);
        TextMessage message = (TextMessage) consumer.receive();
        // TextMessage message = (TextMessage) consumer.receiveNoWait();
        // TextMessage message = (TextMessage) consumer.receive(5000);
        if(message != null)
            System.out.println(message.getText());
        else
            System.out.println("No Message in the Queue");

        connection.close();
    }
}
