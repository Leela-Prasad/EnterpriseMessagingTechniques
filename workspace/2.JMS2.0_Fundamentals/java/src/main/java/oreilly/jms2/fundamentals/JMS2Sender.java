package oreilly.jms2.fundamentals;

import org.apache.activemq.artemis.ArtemisConstants;
import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;

public class JMS2Sender {

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory cf = ActiveMQJMSClient.createConnectionFactory("tcp://0.0.0.0:61616", "mybroker");
        try(JMSContext jmsContext = cf.createContext();) {
            Queue queue = jmsContext.createQueue("TRADE.Q");
            jmsContext
                    .createProducer()
                    .setProperty("TraderName", "Leela")
                    .send(queue, "Buy Apple 500 Shares");
            System.out.println("Message Sent");
        }

    }
}
