package dev.thanbv1510.eventmonitoringsamplev2.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class LogInsertProperties {
    @Value("${log-insert.batch-size}")
    private int batchSize;

    @Value("${log-insert.retry-time}")
    private int numRetry;

    @Value("${log-insert.retry-sleep}")
    private long retrySleepTimeMs;

    @Value("${log-insert.pool.core-size}")
    private int threadInsertNum;
}
