package oreilly.video.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSDurableSubscriber2 implements MessageListener {

    public JMSDurableSubscriber2() throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616?jms.clientID=hostname:2");
        TopicConnection connection = cf.createTopicConnection();
        connection.start();
        TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        //TopicSession session = connection.createTopicSession(false, Session.CLIENT_ACKNOWLEDGE);
        Topic topic = session.createTopic("EM_TRADE.T");
        TopicSubscriber subscriber = session.createDurableSubscriber(topic, "sub:2");
        //TopicSubscriber subscriber = session.createDurableSubscriber(topic, "sub:2", "TradeName = 'AppleShares2'", false);
        subscriber.setMessageListener(this);
        System.out.println("Waiting for Messages");
    }

    public void onMessage(Message message) {
       TextMessage textMessage = (TextMessage) message;
       try {
            System.out.println(textMessage.getText());
            //System.out.println("Delivery Count : " + message.getStringProperty("JMSXDeliveryCount"));
            //if(true) {
            //    throw new RuntimeException("Oops!!!");
            //}
           //message.acknowledge();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws JMSException {
        new JMSDurableSubscriber2();
    }
}
