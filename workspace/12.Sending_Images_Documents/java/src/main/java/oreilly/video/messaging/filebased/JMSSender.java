package oreilly.video.messaging.filebased;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.*;
import java.util.UUID;

public class JMSSender {

    private static String sourceFile = "/Users/Leela/Desktop/Enterprise-Messaging/workspace/12.Sending_Images_Documents/java/src/main/resources/sourceDir/pint.jpg";
    private static String tempDir = "/Users/Leela/Desktop/Enterprise-Messaging/workspace/12.Sending_Images_Documents/java/src/main/resources/tempDir/";

    public static void main(String[] args) throws JMSException, IOException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("EM_IMAGE.Q");
        MessageProducer sender = session.createProducer(queue);

        byte[] sourceFileContent = readFileFromSource();
        String filename = writeToTempFile(sourceFileContent);
        TextMessage msg = session.createTextMessage(filename);
        sender.send(msg);
        System.out.println("Message Sent");

        connection.close();

    }

    private static String writeToTempFile(byte[] bytes) throws IOException {
        String filename = tempDir + UUID.randomUUID().toString();
        File file = new File(filename);
        try(FileOutputStream os = new FileOutputStream(file);) {
            os.write(bytes);
            return filename;
        }
    }

    private static byte[] readFileFromSource() throws IOException {

        File file = new File(sourceFile);
        try (FileInputStream is = new FileInputStream(file);) {
            byte[] bytes = new byte[(int) file.length()];
            is.read(bytes);
            return bytes;
        }
    }
}
