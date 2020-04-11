package oreilly.jms2.features.deliverycount;

import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnection;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;

public class JMS2Publisher {

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory cf = ActiveMQJMSClient.createConnectionFactory("tcp://0.0.0.0:61616", "mybroker");
        try(JMSContext jmsContext = cf.createContext();) {
            Queue queue = jmsContext.createQueue("EM_JMS2_TRADE.Q");

            jmsContext
                    .createProducer()
                    .send(queue, "Buy Appl 1000 Shares");

            System.out.println("Message Sent");
        }

    }
}
