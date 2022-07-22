package dev.thanbv1510.eventmonitoringsamplev2.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mq")
public class MQProperties {
    private String host;

    private int port;

    private String queueManager;

    private String queue;

    private String channel;

    private Long receiveTimeout;
}
