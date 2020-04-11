package oreilly.video.messaging.embedded;

import javafx.scene.text.Text;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSVMReceiver implements MessageListener  {

    private int id;

    public JMSVMReceiver(int id) throws JMSException {
        this.id = id;
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("vm://embedded1");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("TRADE.Q");

        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(this);
        System.out.println("Receiver (" + id + ")" + "Waiting for Messages ...");
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage msg = (TextMessage) message;
            Thread.sleep(1000);
            System.out.println("Trade Received (" + id + ")" + msg.getText());
        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
