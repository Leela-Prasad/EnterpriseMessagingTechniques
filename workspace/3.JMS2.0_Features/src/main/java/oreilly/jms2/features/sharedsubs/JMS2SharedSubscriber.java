package oreilly.jms2.features.sharedsubs;

import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMS2SharedSubscriber {

    /*public static void main(String[] args) throws Exception {
        new Thread() {
            public void run() {
                try {
                    new JMS2SharedSubscriber();
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

    public JMS2SharedSubscriber() throws Exception {
        ActiveMQConnectionFactory cf = ActiveMQJMSClient.createConnectionFactory("tcp://0.0.0.0:61616", "mybroker");
        try(JMSContext jmsContext = cf.createContext();) {
            Topic topic = jmsContext.createTopic("EM_JMS2_TRADE.T");
             jmsContext
                     .createSharedConsumer(topic, "subq")
                     .setMessageListener(this);
            System.out.println("Waiting for Messages (Shared Consumer) ");
        }
    }*/

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory cf = ActiveMQJMSClient.createConnectionFactory("tcp://0.0.0.0:61616", "mybroker");
        try(JMSContext jmsContext = cf.createContext();) {
            Topic topic = jmsContext.createTopic("EM_JMS2_TRADE.T");
            JMSConsumer jmsConsumer = jmsContext.createSharedConsumer(topic, "subq");
            System.out.println("Waiting for Messages (Shared Consumer) ");
            while(true) {
                Message message = jmsConsumer.receiveNoWait();
                if(message!=null) {
                    System.out.println(message.getBody(String.class));
                }
            }
        }
    }

}
