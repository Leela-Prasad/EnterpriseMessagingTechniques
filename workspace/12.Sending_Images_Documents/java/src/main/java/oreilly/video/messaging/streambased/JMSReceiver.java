package oreilly.video.messaging.streambased;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.*;

public class JMSReceiver implements MessageListener {

    private String targetFile = "/Users/Leela/Desktop/Enterprise-Messaging/workspace/12.Sending_Images_Documents/java/src/main/resources/targetDir/sample.pdf";
    //private String targetFile = "/Users/Leela/Desktop/Enterprise-Messaging/workspace/12.Sending_Images_Documents/java/src/main/resources/targetDir/sample.MP4";
    private  FileOutputStream os;

    public JMSReceiver() throws JMSException, FileNotFoundException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("EM_IMAGE.Q");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(this);
        System.out.println("Waiting for Messages");
    }

    public void onMessage(Message message) {
        try {
            if(message.propertyExists("SequenceMarker")) {
                String marker = message.getStringProperty("SequenceMarker");
                if(marker.equals("START")) {
                   os = new FileOutputStream(targetFile);
                }

                if(marker.equals("END")) {
                    os.close();
                    Runtime.getRuntime().exec("open -a Preview " + targetFile);
                }
            }else {
                BytesMessage bytesMessage = (BytesMessage) message;
                System.out.println("Received Message");
                byte[] bytes = new byte[(int) bytesMessage.getBodyLength()];
                bytesMessage.readBytes(bytes);
                os.write(bytes);
            }

        } catch (JMSException | IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws JMSException, FileNotFoundException {
        new JMSReceiver();
    }
}
