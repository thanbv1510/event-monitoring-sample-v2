package dev.thanbv1510.eventmonitoringsamplev2.config;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsOperations;
import org.springframework.jms.core.JmsTemplate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MQConfiguration {
    private final MQProperties mqProperties;

    @Bean
    public MQQueueConnectionFactory mqQueueConnectionFactory() {
        MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
        mqQueueConnectionFactory.setHostName(mqProperties.getHost());
        try {
            mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            mqQueueConnectionFactory.setCCSID(1208);
            mqQueueConnectionFactory.setChannel(mqProperties.getChannel());
            mqQueueConnectionFactory.setPort(mqProperties.getPort());
            mqQueueConnectionFactory.setQueueManager(mqProperties.getQueueManager());
        } catch (Exception e) {
            log.error("", e);
        }

        return mqQueueConnectionFactory;
    }

    @Bean
    @Primary
    public CachingConnectionFactory cachingConnectionFactory(MQQueueConnectionFactory mqQueueConnectionFactory) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(mqQueueConnectionFactory);
        cachingConnectionFactory.setSessionCacheSize(500);
        cachingConnectionFactory.setReconnectOnException(true);
        return cachingConnectionFactory;
    }

    @Bean
    public JmsOperations jmsOperations(CachingConnectionFactory cachingConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory);
        jmsTemplate.setReceiveTimeout(mqProperties.getReceiveTimeout());
        return jmsTemplate;
    }
}
