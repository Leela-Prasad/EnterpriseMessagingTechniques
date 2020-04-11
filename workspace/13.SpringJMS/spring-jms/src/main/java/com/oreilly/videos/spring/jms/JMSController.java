package com.oreilly.videos.spring.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JMSController {

    @Autowired
    private JMSSender jmsSender;

    @GetMapping("/send")
    public void send() {
        jmsSender.sendMessage();
    }
}
