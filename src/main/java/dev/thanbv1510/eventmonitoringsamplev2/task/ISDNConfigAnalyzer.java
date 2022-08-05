package dev.thanbv1510.eventmonitoringsamplev2.task;

import dev.thanbv1510.eventmonitoringsamplev2.listener.ISDNAnalyzerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ISDNConfigAnalyzer {
    private final ISDNAnalyzerService isdnAnalyzerService;

    @Scheduled(initialDelayString = "${isdn-analyzer.initial-delay}", fixedDelayString = "${isdn-analyzer.fixed-delay}")
    public void process() {
        isdnAnalyzerService.refreshISDNConfigs();
    }
}
