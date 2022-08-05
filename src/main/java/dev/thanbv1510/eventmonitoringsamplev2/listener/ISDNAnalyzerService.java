package dev.thanbv1510.eventmonitoringsamplev2.listener;

import dev.thanbv1510.eventmonitoringsamplev2.domain.dto.ISDNConfigDto;
import dev.thanbv1510.eventmonitoringsamplev2.repository.ISDNConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ISDNAnalyzerService {
    private final ISDNConfigRepository isdnConfigRepository;
    private final List<ISDNConfigDto> isdnConfigs;

    public void refreshISDNConfigs() {
        List<ISDNConfigDto> isdnConfigDtos = isdnConfigRepository.getArrISDNConfig();
        log.info(String.format("==> [Thread: %s]Reload ISDNConfig in database, retrieve data size: %d", Thread.currentThread().getName(), isdnConfigDtos.size()));

        isdnConfigs.clear();
        isdnConfigs.addAll(isdnConfigDtos);
    }

    public String getISDNFromIIBMsg(String msgFlowName, String operation, String xmlMsgDetail) {
        for (ISDNConfigDto config : isdnConfigs) {
            if (config == null || StringUtils.isEmpty(config.getStrBeginIdx())) {
                continue;
            }

            xmlMsgDetail = xmlMsgDetail.replaceAll("\\s+", ""); // remove space
            if (msgFlowName.trim().equalsIgnoreCase(config.getMsgFlowName().trim())
                    && (operation.trim().equalsIgnoreCase(config.getOperation().trim()) || operation.trim().endsWith(config.getOperation().trim()))
                    && xmlMsgDetail.contains(config.getStrBeginIdx())) {
                String subXml = xmlMsgDetail.substring(xmlMsgDetail.indexOf(config.getStrBeginIdx()) + config.getStrBeginIdx().length());

                // Priority to get the length
                if (config.getIsdnLength() != 0) {
                    return subXml.substring(0, config.getIsdnLength());
                }

                if (!StringUtils.isEmpty(config.getStrEndIdx())) {
                    return subXml.substring(0, subXml.indexOf(config.getStrEndIdx()));
                }
            }

        }

        return null;
    }
}
