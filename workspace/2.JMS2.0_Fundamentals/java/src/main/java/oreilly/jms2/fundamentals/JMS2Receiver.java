package oreilly.jms2.fundamentals;

import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Queue;

public class JMS2Receiver {

    public static void main(String[] args) throws Exception {

        ActiveMQConnectionFactory cf = ActiveMQJMSClient.createConnectionFactory("tcp://0.0.0.0:61616", "mybroker");
        try(JMSContext jmsContext = cf.createContext();) {
            Queue queue = jmsContext.createQueue("TRADE.Q");
            Message msg = jmsContext
                                .createConsumer(queue)
                                .receive();
                                //.receiveBody(String.class);

            System.out.println(msg.getBody(String.class) + ", TraderName = " + msg.getStringProperty("TraderName"));
        }
    }
}
