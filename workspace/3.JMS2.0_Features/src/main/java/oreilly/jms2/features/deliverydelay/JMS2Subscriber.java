package oreilly.jms2.features.deliverydelay;

import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMS2Subscriber {

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory cf = ActiveMQJMSClient.createConnectionFactory("tcp://0.0.0.0:61616", "mybroker");
        try (JMSContext jmsContext = cf.createContext();) {
            Queue queue = jmsContext.createQueue("EM_JMS2_TRADE.Q");
            System.out.println("Waiting for message");
            Message message = jmsContext
                                    .createConsumer(queue)
                                    .receive();
            System.out.println(message.getBody(String.class));
        }

    }
    
}
