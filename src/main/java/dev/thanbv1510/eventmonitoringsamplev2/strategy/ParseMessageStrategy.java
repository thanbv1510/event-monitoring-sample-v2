package dev.thanbv1510.eventmonitoringsamplev2.strategy;

import dev.thanbv1510.eventmonitoringsamplev2.domain.dto.LogInfoDto;
import dev.thanbv1510.eventmonitoringsamplev2.utils.ConvertUtils;
import dev.thanbv1510.eventmonitoringsamplev2.utils.DateUtils;
import dev.thanbv1510.eventmonitoringsamplev2.utils.TextUtils;
import dev.thanbv1510.eventmonitoringsamplev2.utils.XmlUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public interface ParseMessageStrategy {
    LogInfoDto parse(String msg);

    static ParseMessageStrategy parseIIBMessage() {
        return msg -> {
            String content = TextUtils.decryptString(XmlUtils.queryDataFromXMLDocument(msg, "/event/bitstreamData/bitstream", null));
            if (StringUtils.isEmpty(content)) {
                content = XmlUtils.queryDataFromXMLDocument(msg, "/event/applicationData/complexContent/LogInfo/ResponseData", null);
            }

            String msgId = XmlUtils.queryDataFromXMLDocument(msg, "/event/applicationData/complexContent/LogInfo/MsgID", null);
            if (StringUtils.isEmpty(msgId)) {
                msgId = XmlUtils.queryDataFromXMLDocument(msg, "/event/applicationData/simpleContent", "wmb:value");
            }

            String logTimeStr = XmlUtils.queryDataFromXMLDocument(msg, "/event/applicationData/complexContent/LogInfo/Time", null);
            String operation = XmlUtils.queryDataFromXMLDocument(msg, "/event/applicationData/complexContent/LogInfo/Operation", null);
            String moduleName = XmlUtils.queryDataFromXMLDocument(msg, "/event/applicationData/complexContent/LogInfo/SystemName", null);

            return LogInfoDto.builder()
                    .resultFlow(ConvertUtils.convert(XmlUtils.queryDataFromXMLDocument(msg, "/event/applicationData/complexContent/LogInfo/ResultFlow", null).trim(), Integer.class).orElse(0))
                    .iibErrCode(ConvertUtils.convert(XmlUtils.queryDataFromXMLDocument(msg, "/event/applicationData/complexContent/LogInfo/ErrCode", null).trim(), Integer.class).orElse(0))
                    .command(operation)
                    .flowName(XmlUtils.queryDataFromXMLDocument(msg, "/event/applicationData/complexContent/LogInfo/SystemName", null))
                    .contentType(XmlUtils.queryDataFromXMLDocument(msg, "/event/applicationData/complexContent/LogInfo/ContentType", null))
                    .description("")
                    .stepName(XmlUtils.queryDataFromXMLDocument(msg, "/event/eventPointData/eventData/eventIdentity", "wmb:eventName"))
                    .infoLevel(ConvertUtils.convert(XmlUtils.queryDataFromXMLDocument(msg, "/event/applicationData/complexContent/LogInfo/LogLevel", null), String.class).orElse("").equals("ERROR") ? 2 : 1)
                    .serverName(XmlUtils.queryDataFromXMLDocument(msg, "/event/applicationData/complexContent/LogInfo/ServerName", null))
                    .msgContent(content)
                    .clientIp(XmlUtils.queryDataFromXMLDocument(msg, "/event/applicationData/complexContent/LogInfo/RemoteAddr", null))
                    .username(XmlUtils.queryDataFromXMLDocument(msg, "/event/applicationData/complexContent/LogInfo/User", null))
                    .logTime(new Date())
                    .msgId(TextUtils.getValueBetweenSingleQuotes(msgId))
                    .msgTime(DateUtils.formatToDate(logTimeStr))
                    .msisdn(null)
                    .mainMsgErr(XmlUtils.queryDataFromXMLDocument(msg, "/event/applicationData/complexContent/LogInfo/MainMsgError", null))
                    .build();
        };
    }
}
