package com.oreilly.videos.spring.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Component
public class JMSReceiver {

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "EM_TRADE.Q", selector = "TraderName = 'Mark'", concurrency = "20-20")
    public void placeTrade(@Payload String trade, @Headers MessageHeaders headers, Message message) throws JMSException {
       // System.out.println("Processing Trade: " + trade + ", Trader = " + headers.get("TraderName"));
        System.out.println("Processing Trade: " + ((TextMessage)message).getText() + ", Trader = " + message.getStringProperty("TraderName"));
    }
}
