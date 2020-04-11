package oreilly.jms2.features.sharedsubs;

import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.Topic;
import java.text.DecimalFormat;

public class JMS2SharedPublisher {

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory cf = ActiveMQJMSClient.createConnectionFactory("tcp://0.0.0.0:61616", "mybroker");
        try(JMSContext jmsContext = cf.createContext();) {
            Topic topic = jmsContext.createTopic("EM_JMS2_TRADE.T");
            DecimalFormat df = new DecimalFormat("##.00");
            String price = df.format(98.0 + Math.random());
            jmsContext
                    .createProducer()
                    .send(topic, price);
            System.out.println("Message Published");
        }

    }
}
