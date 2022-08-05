package dev.thanbv1510.eventmonitoringsamplev2.repository;

import dev.thanbv1510.eventmonitoringsamplev2.domain.dto.LogInfoDto;

import java.util.List;

public interface LogInfoRepository {
    int callProcsBatchInsert(List<LogInfoDto> logInfoDtos);
}
