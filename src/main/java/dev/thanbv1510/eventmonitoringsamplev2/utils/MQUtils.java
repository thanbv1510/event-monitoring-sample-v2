package dev.thanbv1510.eventmonitoringsamplev2.utils;

import com.ibm.jms.JMSBytesMessage;
import com.ibm.jms.JMSTextMessage;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@UtilityClass
public class MQUtils {
    private static final Map<Class<?>, Function<Message, String>> doGetBodyMsg = new HashMap<>();

    static {
        doGetBodyMsg.put(JMSBytesMessage.class, MQUtils::getBodyFromJMSBytesMessage);
        doGetBodyMsg.put(JMSTextMessage.class, MQUtils::getBodyFromJMSTextMessage);
    }

    public static Optional<String> getBodyMsg(Message message) {
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
