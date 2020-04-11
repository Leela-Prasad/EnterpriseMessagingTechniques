package oreilly.video.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSReceiver {

    public static void main(String[] args) throws JMSException {

        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
        Queue queue = session.createQueue("TRADE.Q");

        MessageConsumer consumer = session.createConsumer(queue);
        while(true) {
            TextMessage message = (TextMessage) consumer.receiveNoWait();
            if(message!=null) {
                displayHeaders(message);
                session.rollback();
                System.out.println(message.getText());
            }
        }



        //connection.close();
    }

    private static void displayHeaders(TextMessage message) throws JMSException {
        System.out.println("Message ID: " + message.getJMSMessageID());
        System.out.println("Correlation ID: " + message.getJMSCorrelationID());
        System.out.println("JMS Delivery Mode : " + message.getJMSDeliveryMode());
        System.out.println("JMS Expiration : " + message.getJMSExpiration());
        System.out.println("JMS Priority : " + message.getJMSPriority());
        System.out.println("Redelivered? " + message.getJMSRedelivered());
        System.out.println("JMS Timestamp : " + message.getJMSTimestamp());
        System.out.println("JMS Destination : " + message.getJMSDestination());
        System.out.println("JMS Reply To : " + message.getJMSReplyTo());
        System.out.println("JMSXDeliveryCount: " + message.getIntProperty("JMSXDeliveryCount"));
    }
}
