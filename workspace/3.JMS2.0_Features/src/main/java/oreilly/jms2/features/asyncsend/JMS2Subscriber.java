package oreilly.jms2.features.asyncsend;

import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMS2Subscriber {

    /* private JMSContext jmsContext = null;

    public static void main(String[] args) {
        new Thread() {
            public void run() {
                try {
                    new JMS2Subscriber();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onMessage(Message message) {
        try {
            System.out.println(message.getBody(String.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JMS2Subscriber() throws Exception {
        ActiveMQConnectionFactory cf = ActiveMQJMSClient.createConnectionFactory("tcp://0.0.0.0:61616", "mybroker");
        try {
            jmsContext = cf.createContext();
            Queue queue = jmsContext.createQueue("EM_JMS2_TRADE.Q");
             jmsContext
                     .createConsumer(queue)
                     .setMessageListener(this);
            System.out.println("Waiting for Messages");
        } catch (Exception e) {
            e.printStackTrace();
        }
    } */

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory cf = ActiveMQJMSClient.createConnectionFactory("tcp://0.0.0.0:61616", "mybroker");
        try (JMSContext jmsContext = cf.createContext();) {

            Queue queue = jmsContext.createQueue("EM_JMS2_TRADE.Q");
            System.out.println("Waiting for Messages");
            JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);
            while(true) {
                Message message = jmsConsumer.receiveNoWait();
                if(message!=null) {
                    System.out.println(message.getBody(String.class));
                }
            }

        }
    }

    
}
