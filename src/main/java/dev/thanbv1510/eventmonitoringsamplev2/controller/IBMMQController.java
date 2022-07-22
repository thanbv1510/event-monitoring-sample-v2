package dev.thanbv1510.eventmonitoringsamplev2.controller;

import dev.thanbv1510.eventmonitoringsamplev2.config.MQProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/ibm-mq")
@RequiredArgsConstructor
public class IBMMQController {
    private final JmsOperations jmsOperations;
    private final MQProperties mqProperties;

    @GetMapping("/send-msg")
    public String send() {
        try {
            jmsOperations.convertAndSend(mqProperties.getQueue(), "Hello World!");
            return "OK";
        } catch (JmsException ex) {
            log.error("", ex);
            return "FAIL";
        }
    }

    @GetMapping("receive-msg")
    public String receive() {
        try {
            Object msg = jmsOperations.receiveAndConvert(mqProperties.getQueue());
            if (Objects.nonNull(msg)) {
                return msg.toString();
            }
        } catch (JmsException ex) {
            log.error("", ex);
            return "FAIL";
        }

        return null;
    }
}
