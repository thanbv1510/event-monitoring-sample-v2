package dev.thanbv1510.eventmonitoringsamplev2.config;

import dev.thanbv1510.eventmonitoringsamplev2.domain.dto.ISDNConfigDto;
import dev.thanbv1510.eventmonitoringsamplev2.domain.dto.LogInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class BeansConfig {
    @Value("${log-listening.pool.min-size}")
    private Integer minPoolSize;

    @Value("${log-listening.pool.max-size}")
    private Integer maxPoolSize;

    @Value("${log-listening.pool.queue-capacity}")
    private Integer queueCapacity;

    @Bean("ProcessLogInfoExecutor")
    public Executor processLogInfoExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(minPoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix("ProcessLogInfoExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    public BlockingQueue<LogInfoDto> logInfoQueues() {
        return new LinkedBlockingQueue<>();
    }

    @Bean
    public List<ISDNConfigDto> isdnConfigs() {
        return new ArrayList<>();
    }
}
