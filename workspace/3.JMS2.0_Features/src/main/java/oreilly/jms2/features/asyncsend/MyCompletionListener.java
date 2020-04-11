package oreilly.jms2.features.asyncsend;

import javax.jms.CompletionListener;
import javax.jms.Message;
import java.util.concurrent.CountDownLatch;

public class MyCompletionListener implements CompletionListener {

    private CountDownLatch latch = null;

    public MyCompletionListener(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onCompletion(Message message) {
        latch.countDown();
        System.out.println("message acknowledged by server");

    }

    @Override
    public void onException(Message message, Exception e) {
        latch.countDown();
        System.out.println("unable to send message " + e);

    }
}
