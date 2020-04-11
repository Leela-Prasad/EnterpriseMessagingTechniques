package oreilly.jms2.features.sharedsubs;

import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMS2Subscriber {

    /* public static void main(String[] args) throws Exception {
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
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public JMS2Subscriber() throws Exception {
        ActiveMQConnectionFactory cf = ActiveMQJMSClient.createConnectionFactory("tcp://0.0.0.0:61616", "mybroker");
        try(JMSContext jmsContext = cf.createContext();) {
            Topic topic = jmsContext.createTopic("EM_JMS2_TRADE.T");
             jmsContext
                     .createConsumer(topic)
                     .setMessageListener(this);
            System.out.println("Waiting for Messages (Normal Consumer) ");
        }
    } */

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory cf = ActiveMQJMSClient.createConnectionFactory("tcp://0.0.0.0:61616", "mybroker");
        try(JMSContext jmsContext = cf.createContext();) {
            Topic topic = jmsContext.createTopic("EM_JMS2_TRADE.T");
            JMSConsumer jmsConsumer = jmsContext.createConsumer(topic);
            System.out.println("Waiting for Messages (Normal Consumer) ");
            while(true) {
                Message message = jmsConsumer.receiveNoWait();
                if(message!=null) {
                    System.out.println(message.getBody(String.class));
                }
            }
        }
    }

    
}
