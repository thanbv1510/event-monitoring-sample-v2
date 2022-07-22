package dev.thanbv1510.eventmonitoringsamplev2.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Slf4j
@Component
public class MQListener {
    @JmsListener(destination = "${mq.queue}")
    public void receive(Message message) throws JMSException {
        TextMessage textMessage = (TextMessage) message;
        log.info("==> JmsListener: receive message: {}",
                textMessage.getText());

        // do some business logic here, like updating the order in the database
    }
}
