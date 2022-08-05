package dev.thanbv1510.eventmonitoringsamplev2.service;

import dev.thanbv1510.eventmonitoringsamplev2.domain.dto.LogInfoDto;
import dev.thanbv1510.eventmonitoringsamplev2.listener.ISDNAnalyzerService;
import dev.thanbv1510.eventmonitoringsamplev2.repository.LogInfoRepository;
import dev.thanbv1510.eventmonitoringsamplev2.strategy.ParseMessageStrategy;
import dev.thanbv1510.eventmonitoringsamplev2.utils.TextUtils;
import dev.thanbv1510.eventmonitoringsamplev2.utils.XmlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogInfoService {
    private final ISDNAnalyzerService isdnAnalyzerService;
    private final BlockingQueue<LogInfoDto> logInfoQueues;
    private final LogInfoRepository logInfoRepository;

    public void saveAllLogInfo(List<LogInfoDto> logInfoDtos) {
        logInfoRepository.callProcsBatchInsert(logInfoDtos);
    }

    @Async("ProcessLogInfoExecutor")
    public void processingLogInfo(String bodyMsg) {
        ParseMessageStrategy parseIIBMessage = ParseMessageStrategy.parseIIBMessage();
        LogInfoDto logInfoDto = parseIIBMessage.parse(bodyMsg);
        String content = TextUtils.decryptString(XmlUtils.queryDataFromXMLDocument(bodyMsg, "/event/bitstreamData/bitstream", null));
        if (Objects.isNull(logInfoDto.getMsisdn())) {
            logInfoDto.setMsisdn(isdnAnalyzerService.getISDNFromIIBMsg(logInfoDto.getFlowName(), logInfoDto.getCommand(), content));
        }

        if (StringUtils.isNotEmpty(logInfoDto.getMsgId())) {
            log.info("==> Receive LogInfo data: {}", logInfoDto);
            logInfoQueues.add(logInfoDto);
        } else {
            log.warn("==> Receive data with MsgId is null, Ignore!");
        }
    }
}
