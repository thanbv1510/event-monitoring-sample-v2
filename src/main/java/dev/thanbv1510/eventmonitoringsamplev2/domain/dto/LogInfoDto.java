package dev.thanbv1510.eventmonitoringsamplev2.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;


@Data
@Builder
public class LogInfoDto {
    private String msgId;
    private String clientIp;
    private String username;
    private Date logTime;
    private String msisdn;
    private String command;
    private Integer resultFlow;
    private String description;
    private Integer infoLevel;
    private String stepName;
    private String contentType;
    private String serverName;
    private String msgContent;
    private Date msgTime;
    private String flowName;
    private String mainMsgErr;
    private Integer iibErrCode;
}
