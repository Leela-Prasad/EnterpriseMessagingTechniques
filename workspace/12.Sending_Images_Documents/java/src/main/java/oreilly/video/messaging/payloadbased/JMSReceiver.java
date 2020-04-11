package oreilly.video.messaging.payloadbased;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class JMSReceiver implements MessageListener {

    private String targetFile = "/Users/Leela/Desktop/Enterprise-Messaging/workspace/12.Sending_Images_Documents/java/src/main/resources/targetDir/pint.jpg";
    // private String targetFile = "/Users/Leela/Desktop/Enterprise-Messaging/workspace/12.Sending_Images_Documents/java/src/main/resources/targetDir/sample.pdf";


    public JMSReceiver() throws JMSException {
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
        File file = new File(targetFile);
        System.out.println("File Deleted? " + file.delete());
        BytesMessage bytesMessage = (BytesMessage) message;
        try {
            byte[] bytes = new byte[(int) bytesMessage.getBodyLength()];
            bytesMessage.readBytes(bytes);
            writeToTargetFile(bytes);
            Runtime.getRuntime().exec("open -a Preview " + targetFile);
        } catch (JMSException | IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToTargetFile(byte[] bytes) throws IOException {
        File file = new File(targetFile);
        try(FileOutputStream os = new FileOutputStream(file);) {
            os.write(bytes);
        }
    }

    public static void main(String[] args) throws JMSException {
        new JMSReceiver();
    }
}
