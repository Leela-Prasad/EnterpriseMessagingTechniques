package oreilly.video.messaging.embedded;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.activemq.broker.BrokerService;

import javax.jms.JMSException;
import java.util.Arrays;
import java.util.List;

public class JMSVMApplication {

    private List<String> trades = Arrays.asList("BUY APPL 2000", "BUY IBM 4400", "BUY ATT 2400",
                                                "SELL AAPL 1000", "SELL IBM 2200", "SELL ATT 1200");

    public static void main(String[] args) throws Exception {
        JMSVMApplication app = new JMSVMApplication();
        app.startBroker();
        app.startTradeProcessors();
        app.processTrades();
    }

    private void processTrades() throws JMSException {
        JMSVMSender sender = new JMSVMSender();
        for(String trade : trades) {
            sender.sendMessage(trade);
        }
    }

    private void startTradeProcessors() throws JMSException {
        new JMSVMReceiver(1);
        new JMSVMReceiver(2);
        new JMSVMReceiver(3);
        new JMSVMReceiver(4);
        new JMSVMReceiver(5);
        new JMSVMReceiver(6);

    }

    private void startBroker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://localhost:61888");
        broker.setBrokerName("embedded1");
        broker.setPersistent(false);
        broker.start();
    }
}
