package dev.thanbv1510.eventmonitoringsamplev2.listener;

import com.ibm.jms.JMSBytesMessage;
import com.ibm.jms.JMSTextMessage;
import dev.thanbv1510.eventmonitoringsamplev2.domain.dto.LogInfoDto;
import dev.thanbv1510.eventmonitoringsamplev2.strategy.ParseMessageStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@Component
public class MQListener {
    private static final Map<Class<?>, Function<Message, String>> doGetBodyMsg = new HashMap<>();

    static {
        doGetBodyMsg.put(JMSBytesMessage.class, MQListener::getBodyFromJMSBytesMessage);
        doGetBodyMsg.put(JMSTextMessage.class, MQListener::getBodyFromJMSTextMessage);
    }

    @JmsListener(destination = "${mq.queue}")
    public void receive(Message message) {
        Optional<String> bodyMsgOptional = getBodyMsg(message);
        if (!bodyMsgOptional.isPresent()) {
            log.warn("==> JmsListener: Received a message with an unsupported format!");
            return;
        }

        String bodyMsg = bodyMsgOptional.get();
        log.info("==> JmsListener: receive message: {}", bodyMsg);

        ParseMessageStrategy parseIIBMessage = ParseMessageStrategy.parseIIBMessage();
        LogInfoDto logInfoDto = parseIIBMessage.parse(bodyMsg);
        log.info("==> Receive data: {}", logInfoDto);
    }

    private Optional<String> getBodyMsg(Message message) {
        Function<Message, String> handler = doGetBodyMsg.get(message.getClass());
        if (Objects.nonNull(handler)) {
            return Optional.ofNullable(handler.apply(message));
        }

        return Optional.empty();
    }

    private static String getBodyFromJMSBytesMessage(Message message) {
        try {
            JMSBytesMessage byteMessage = (JMSBytesMessage) message;
            byte[] byteData;
            byteData = new byte[(int) byteMessage.getBodyLength()];
            byteMessage.readBytes(byteData);
            byteMessage.reset();
            return new String(byteData);
        } catch (JMSException ex) {
            log.error("", ex);
            return null;
        }
    }

    private static String getBodyFromJMSTextMessage(Message message) {
        try {
            return message.getBody(String.class);
        } catch (JMSException ex) {
            log.error("", ex);
            return null;
        }
    }
}
