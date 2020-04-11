package oreilly.video.messaging.req.res.temp.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSSender {

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        QueueConnection connection = cf.createQueueConnection();
        connection.start();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("EM_TRADE_REQ.Q");

        TextMessage message = session.createTextMessage();
        message.setText("Buy APPL 1000 Shares");

        QueueRequestor queueRequestor = new QueueRequestor(session, queue);
        TextMessage response = (TextMessage) queueRequestor.request(message);

        System.out.println(response.getText());


        connection.close();

    }
}
