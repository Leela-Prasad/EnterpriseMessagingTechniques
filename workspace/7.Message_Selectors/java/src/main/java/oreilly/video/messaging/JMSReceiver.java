package oreilly.video.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSReceiver {

    public static void main(String[] args) throws JMSException {

        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("EM_JMS_FILTER.Q");

        // MessageConsumer consumer = session.createConsumer(queue, "State = 'open'");
        // MessageConsumer consumer = session.createConsumer(queue, "State = 'placed'");
        // MessageConsumer consumer = session.createConsumer(queue, "Validated > TRUE"); // Invalid Selector Expression
        MessageConsumer consumer = session.createConsumer(queue, "Validated = TRUE");
        System.out.println("filter: " + consumer.getMessageSelector());
        TextMessage message = (TextMessage) consumer.receive();
        System.out.println(message.getText());


        connection.close();
    }
}
