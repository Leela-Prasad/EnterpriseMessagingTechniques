package oreilly.video.messaging.req.res.custom.property.processing;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.UUID;

public class JMSSender {

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queueReq = session.createQueue("EM_TRADE_REQ.Q");
        Queue queueRes = session.createQueue("EM_TRADE_RES.Q");

        TextMessage message = session.createTextMessage();
        message.setText("Request-Reply: Buy APPL 1000 Shares");
        message.setJMSReplyTo(queueRes);
        String uuid = UUID.randomUUID().toString();
        message.setStringProperty("uuid", uuid);

        MessageProducer sender = session.createProducer(queueReq);
        sender.send(message);
        System.out.println("Request-Reply: Message Sent");


        String filter = "MessageLink = '" + uuid + "'";
        MessageConsumer consumer = session.createConsumer(queueRes, filter);
        TextMessage response = (TextMessage) consumer.receive();
        System.out.println("Confirmation = " + response.getText());

        // Here Session will also be closed as part of connection close
        connection.close();

    }
}