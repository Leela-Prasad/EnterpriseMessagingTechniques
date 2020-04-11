package oreilly.video.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSSubscriber implements MessageListener {

    public JMSSubscriber() throws JMSException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        TopicConnection connection = cf.createTopicConnection();
        connection.start();
        TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("EM_TRADE.T");
        TopicSubscriber subscriber = session.createSubscriber(topic);
        subscriber.setMessageListener(this);
        System.out.println("Waiting for Messages");
    }

    public void onMessage(Message message) {
       TextMessage textMessage = (TextMessage) message;
       try {
            System.out.println(textMessage.getText());
            //if(true) {
            //    throw new RuntimeException("Oops!!!");
            //}
           //message.acknowledge();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws JMSException {
        new JMSSubscriber();
    }
}
