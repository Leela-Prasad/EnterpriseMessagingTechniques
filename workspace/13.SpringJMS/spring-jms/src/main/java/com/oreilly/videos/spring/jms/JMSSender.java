package com.oreilly.videos.spring.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

@Component
public class JMSSender {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage() {
        //jmsTemplate.convertAndSend("EM_TRADE.Q", "Buy APPL 1200 Shares");

        /* MessageCreator mc = new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage msg = session.createTextMessage("Buy APPL 1500 Shares");
                msg.setStringProperty("TraderName", "Mark");
                return msg;
            }
        };

        jmsTemplate.send("EM_TRADE.Q", mc); */

        MessagePostProcessor mpp = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("TraderName", "Mark");
                return message;
            }
        };

        jmsTemplate.convertAndSend("EM_TRADE.Q", "Buy IBM 2000 Shares", mpp);
        
        System.out.println("Message Sent");
    }
}
