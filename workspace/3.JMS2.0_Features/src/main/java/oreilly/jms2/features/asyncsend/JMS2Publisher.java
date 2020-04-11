package oreilly.jms2.features.asyncsend;

import org.apache.activemq.artemis.api.jms.ActiveMQJMSClient;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.JMSContext;
import javax.jms.Queue;
import java.util.concurrent.CountDownLatch;

public class JMS2Publisher {

    public static void main(String[] args) throws Exception {
        ActiveMQConnectionFactory cf = ActiveMQJMSClient.createConnectionFactory("tcp://0.0.0.0:61616", "mybroker");
        try(JMSContext jmsContext = cf.createContext();) {
            Queue queue = jmsContext.createQueue("EM_JMS2_TRADE.Q");

            CountDownLatch latch = new CountDownLatch(1);
            MyCompletionListener cl = new MyCompletionListener(latch);
            jmsContext
                    .createProducer()
                    .setAsync(cl)
                    .send(queue, "Buy Appl 1000 Shares");
            System.out.println("Message Sent");

            for (int i=0;i<5;i++) {
                System.out.println("processing...");
                Thread.sleep(1000);
            }

            latch.await();

            System.out.println("end processing");
        }

    }
}
