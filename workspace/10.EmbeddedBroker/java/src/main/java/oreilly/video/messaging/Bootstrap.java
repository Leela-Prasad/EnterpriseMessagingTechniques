package oreilly.video.messaging;

import org.apache.activemq.broker.BrokerService;

public class Bootstrap {

    public static void main(String[] args) throws Exception {
        new Bootstrap().startBroker();
    }

    private void startBroker() throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector("tcp://localhost:61888");
        broker.setBrokerName("embedded1");
        broker.setPersistent(false);
        broker.start();
        System.out.println("Embedded Broker Started");
    }
}
