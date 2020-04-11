package oreilly.video.messaging.filebased;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.*;
import java.util.UUID;

public class JMSReceiver implements MessageListener {

    private String targetFile = "/Users/Leela/Desktop/Enterprise-Messaging/workspace/12.Sending_Images_Documents/java/src/main/resources/targetDir/pint.jpg";

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
       TextMessage textMessage = (TextMessage) message;
       try {
            String filename = textMessage.getText();
            System.out.println("Message Received : " + filename);
            byte[] bytes = readFileFromSource(filename);
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

    private byte[] readFileFromSource(String filename) throws IOException {
        File file = new File(filename);
        try (FileInputStream is = new FileInputStream(file);) {
            byte[] bytes = new byte[(int) file.length()];
            is.read(bytes);
            return bytes;
        }
    }

    public static void main(String[] args) throws JMSException {
        new JMSReceiver();
    }
}
