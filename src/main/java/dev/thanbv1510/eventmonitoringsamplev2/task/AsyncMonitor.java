package dev.thanbv1510.eventmonitoringsamplev2.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncMonitor {
    private final ApplicationContext applicationContext;

    @Scheduled(initialDelayString = "${async-monitor.initial-delay}", fixedDelayString = "${async-monitor.fixed-delay}")
    public void printExecutorStatistic() {
        try {
            Map<String, ThreadPoolTaskExecutor> executorBeans =
                    applicationContext.getBeansOfType(ThreadPoolTaskExecutor.class);
            executorBeans
                    .keySet()
                    .forEach(
                            poolName -> {
                                ThreadPoolTaskExecutor executor = executorBeans.get(poolName);
                                log.info(
                                        "==> Pool Monitor {}: ActiveCount {}, QueueSize {}, TaskCount {}, CompletedTaskCount {}, PoolSize {}, LargestPoolSize {}, CorePoolSize {}, MaxPoolSize {} ",
                                        poolName,
                                        executor.getThreadPoolExecutor().getActiveCount(),
                                        executor.getThreadPoolExecutor().getQueue().size(),
                                        executor.getThreadPoolExecutor().getTaskCount(),
                                        executor.getThreadPoolExecutor().getCompletedTaskCount(),
                                        executor.getThreadPoolExecutor().getPoolSize(),
                                        executor.getThreadPoolExecutor().getLargestPoolSize(),
                                        executor.getThreadPoolExecutor().getCorePoolSize(),
                                        executor.getThreadPoolExecutor().getMaximumPoolSize());
                            });
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }
}
