package oreilly.video.messaging.payloadbased;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class JMSSender {

    private static String sourceFile = "/Users/Leela/Desktop/Enterprise-Messaging/workspace/12.Sending_Images_Documents/java/src/main/resources/sourceDir/pint.jpg";
    // private static String sourceFile = "/Users/Leela/Desktop/Enterprise-Messaging/workspace/12.Sending_Images_Documents/java/src/main/resources/sourceDir/sample.pdf";

    public static void main(String[] args) throws JMSException, IOException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("EM_IMAGE.Q");
        MessageProducer sender = session.createProducer(queue);

        byte[] sourceFileContent = readFileFromSource();
        BytesMessage msg = session.createBytesMessage();
        msg.writeBytes(sourceFileContent);
        sender.send(msg);

        System.out.println("Message Sent");

        connection.close();
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
