package oreilly.video.messaging.embedded;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSVMSender {

    private MessageProducer sender;
    private Session session;

    public JMSVMSender() throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("vm://embedded1");
        Connection connection = cf.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("TRADE.Q");
        sender = session.createProducer(queue);
    }

    public void sendMessage(String trade) throws JMSException {
        TextMessage message = session.createTextMessage(trade);
        sender.send(message);
        System.out.println("Trade sent");
    }
}
