package oreilly.video.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSAsyncReceiver implements MessageListener {

    public JMSAsyncReceiver() throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue("TRADE.Q");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(this);
        System.out.println("Waiting for Messages");
    }

    public void onMessage(Message message) {
       TextMessage textMessage = (TextMessage) message;
       try {
            System.out.println(textMessage.getText() + ", Trader = " + textMessage.getStringProperty("TraderName"));
            // textMessage.acknowledge();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws JMSException {
        new JMSAsyncReceiver();
    }
}
