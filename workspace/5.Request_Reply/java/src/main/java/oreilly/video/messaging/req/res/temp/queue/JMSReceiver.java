package oreilly.video.messaging.req.res.temp.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSReceiver {

    public static void main(String[] args) throws JMSException, InterruptedException {

        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("EM_TRADE_REQ.Q");

        MessageConsumer consumer = session.createConsumer(queue);
        TextMessage message = (TextMessage) consumer.receive();
        System.out.println("processing trade: " + message.getText());

        Thread.sleep(4000);
        String confirmation = "EQ-2390";
        MessageProducer producer = session.createProducer(message.getJMSReplyTo());
        producer.send(session.createTextMessage(confirmation));

        connection.close();
    }
}
