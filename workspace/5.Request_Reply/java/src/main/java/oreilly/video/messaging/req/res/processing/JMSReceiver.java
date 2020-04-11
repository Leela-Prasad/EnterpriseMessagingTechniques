package oreilly.video.messaging.req.res.processing;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSReceiver {

    public static void main(String[] args) throws JMSException, InterruptedException {

        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queueReq = session.createQueue("EM_TRADE_REQ.Q");


        MessageConsumer consumer = session.createConsumer(queueReq);
        System.out.println("Request-Reply: Waiting for Messages ... ");
        TextMessage message = (TextMessage) consumer.receive();
        System.out.println("Processing Trade: " + message.getText());
        
        if(message.getJMSReplyTo()!=null) {
            Thread.sleep(4000);
            String confirmation = "EQ-3475";
            TextMessage outMsg = session.createTextMessage(confirmation);
            outMsg.setJMSCorrelationID(message.getJMSMessageID());
            MessageProducer producer = session.createProducer(message.getJMSReplyTo());
            producer.send(outMsg);
        }

        connection.close();
    }
}
