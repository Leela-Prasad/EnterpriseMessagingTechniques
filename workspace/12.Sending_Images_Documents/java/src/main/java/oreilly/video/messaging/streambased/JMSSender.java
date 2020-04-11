package oreilly.video.messaging.streambased;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class JMSSender {

    private static String sourceFile = "/Users/Leela/Desktop/Enterprise-Messaging/workspace/12.Sending_Images_Documents/java/src/main/resources/sourceDir/sample.pdf";
    //private static String sourceFile = "/Users/Leela/Desktop/Enterprise-Messaging/workspace/12.Sending_Images_Documents/java/src/main/resources/sourceDir/sample.MP4";
    private static Session session;
    private static MessageProducer sender;
    private static String uuid;

    public static void main(String[] args) throws JMSException, IOException {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
        Connection connection = cf.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("EM_IMAGE.Q");
        sender = session.createProducer(queue);

        uuid = UUID.randomUUID().toString();

        sendStream(null, "START");

        FileInputStream is = new FileInputStream(sourceFile);
        byte[] bytes = new byte[1000000];

        //returns -1 if the file reached EOF
        while(is.read(bytes) >=0) {
            sendStream(bytes, null);
        }

        sendStream(null, "END");

       is.close();
       connection.close();
    }

    private static void sendStream(byte[] bytes, String marker) throws JMSException {

        BytesMessage msg = session.createBytesMessage();
        msg.setStringProperty("JMSXGroupID", uuid);
        if(bytes == null) {
            msg.setStringProperty("SequenceMarker", marker);
            System.out.println("Sending " + marker);
        }else {
            msg.writeBytes(bytes);
            System.out.println("Streaming ... ");
        }
        sender.send(msg);
    }
}
