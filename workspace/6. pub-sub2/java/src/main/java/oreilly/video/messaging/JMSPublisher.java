package oreilly.video.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.text.DecimalFormat;

public class JMSPublisher {

    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("EM_TRADE.T");

        DecimalFormat df = new DecimalFormat("##.00");
        String price = df.format(95.0 + Math.random());
        TextMessage message = session.createTextMessage();
        message.setText("AAPL " + price);
        //message.setStringProperty("TradeName", "AppleShares");

        MessageProducer publisher = session.createProducer(topic);
        publisher.send(message);
        System.out.println("Message Sent");

        connection.close();

    }
}
