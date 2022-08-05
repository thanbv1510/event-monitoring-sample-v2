package dev.thanbv1510.eventmonitoringsamplev2.task;

import dev.thanbv1510.eventmonitoringsamplev2.config.LogInsertProperties;
import dev.thanbv1510.eventmonitoringsamplev2.domain.dto.LogInfoDto;
import dev.thanbv1510.eventmonitoringsamplev2.service.LogInfoService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class LogInfoInsertTask implements Runnable {
    @NonNull
    private final LogInfoService logInfoService;

    @NonNull
    private final BlockingQueue<LogInfoDto> logInfoQueues;

    @NonNull
    private final LogInsertProperties logInsertProperties;

    @Override
    public void run() {
        List<LogInfoDto> data = new ArrayList<>();
        int retryTimeTemp = logInsertProperties.getNumRetry();
        while (!Thread.interrupted()) {
            LogInfoDto logInfo = logInfoQueues.poll();
            if (logInfo != null) {
                data.add(logInfo);
            }

            // retry if data <= 50% of batch size
            if (data.size() <= logInsertProperties.getBatchSize() / 2 && retryTimeTemp > 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(logInsertProperties.getRetrySleepTimeMs());
                    retryTimeTemp--;
                    continue;
                } catch (InterruptedException e) {
                    log.warn("Interrupted!", e);
                    // Restore interrupted state...
                    Thread.currentThread().interrupt();
                }
            }

            if (!data.isEmpty() && (data.size() >= logInsertProperties.getBatchSize() || logInfoQueues.isEmpty())) {
                logInfoService.saveAllLogInfo(data);
                data.clear();
                retryTimeTemp = logInsertProperties.getNumRetry();
            }
        }
    }
}
