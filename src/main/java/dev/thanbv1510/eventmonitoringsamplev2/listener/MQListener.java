package dev.thanbv1510.eventmonitoringsamplev2.listener;

import dev.thanbv1510.eventmonitoringsamplev2.service.LogInfoService;
import dev.thanbv1510.eventmonitoringsamplev2.utils.MQUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MQListener {
    private final LogInfoService logInfoService;

    @JmsListener(destination = "${mq.queue}", concurrency = "${jms-listener.concurrency}")
    public void receive(Message message) {
        Optional<String> bodyMsgOptional = MQUtils.getBodyMsg(message);
        if (!bodyMsgOptional.isPresent()) {
            log.warn("==> JmsListener: Received a message with an unsupported format!");
            return;
        }

        String bodyMsg = bodyMsgOptional.get();
        log.info("==> JmsListener: receive message: {}", bodyMsg);

        logInfoService.processingLogInfo(bodyMsg);
    }
}
