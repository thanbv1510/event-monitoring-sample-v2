package dev.thanbv1510.eventmonitoringsamplev2;

import dev.thanbv1510.eventmonitoringsamplev2.config.LogInsertProperties;
import dev.thanbv1510.eventmonitoringsamplev2.domain.dto.LogInfoDto;
import dev.thanbv1510.eventmonitoringsamplev2.service.LogInfoService;
import dev.thanbv1510.eventmonitoringsamplev2.task.LogInfoInsertTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@EnableJms
@EnableAsync
@EnableScheduling
@RequiredArgsConstructor
@SpringBootApplication
public class EventMonitoringSampleV2Application implements CommandLineRunner {
    private final LogInfoService logInfoService;
    private final BlockingQueue<LogInfoDto> logInfoQueues;
    private final LogInsertProperties logInsertProperties;

    public static void main(String[] args) {
        SpringApplication.run(EventMonitoringSampleV2Application.class, args);
        log.info("--------------------- STARTING APPLICATION ---------------------");
        log.info("--------------------> Max memory: {}M", Runtime.getRuntime().maxMemory() / 1024 / 1024);
    }

    @Override
    public void run(String... args) throws Exception {
        logInfoWriter();
    }

    public void logInfoWriter() {
        int threadInsertNum = logInsertProperties.getThreadInsertNum();
        Executor executor = Executors.newFixedThreadPool(threadInsertNum);
        for (int i = 0; i < threadInsertNum; i++) {
            LogInfoInsertTask logInfoInsertTask = new LogInfoInsertTask(logInfoService, logInfoQueues, logInsertProperties);
            executor.execute(logInfoInsertTask);
        }
    }
}