package dev.thanbv1510.eventmonitoringsamplev2.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ISDNConfigDto {
    private String msgFlowName;
    private String operation;
    private String strBeginIdx;
    private String strEndIdx;
    private int isdnLength;
}
