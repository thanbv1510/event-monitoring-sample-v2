package dev.thanbv1510.eventmonitoringsamplev2.repository.impl;

import dev.thanbv1510.eventmonitoringsamplev2.domain.dto.LogInfoDto;
import dev.thanbv1510.eventmonitoringsamplev2.repository.LogInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogInfoRepositoryImpl implements LogInfoRepository {
    private final DataSource dataSource;

    @Override
    public int callProcsBatchInsert(List<LogInfoDto> logInfoDtos) {
        String sql = "{call PROC_BATCH_INS_LOG_EVT_IIB(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        int[] result;
        try (Connection connection = dataSource.getConnection();
             CallableStatement callableStatement = connection.prepareCall(sql)) {
            connection.setAutoCommit(false);
            for (LogInfoDto logInfoDto : logInfoDtos) {
                callableStatement.setString(1, logInfoDto.getMsgId());
                callableStatement.setString(2, logInfoDto.getClientIp());
                callableStatement.setString(3, logInfoDto.getUsername());
                callableStatement.setTimestamp(4, new Timestamp(logInfoDto.getLogTime().getTime()));
                callableStatement.setTimestamp(5, new Timestamp(logInfoDto.getMsgTime().getTime()));
                callableStatement.setString(6, logInfoDto.getFlowName());
                callableStatement.setString(7, logInfoDto.getMsisdn());
                callableStatement.setString(8, logInfoDto.getMsgContent());
                callableStatement.setInt(9, logInfoDto.getResultFlow());
                callableStatement.setString(10, logInfoDto.getDescription());
                callableStatement.setString(11, logInfoDto.getCommand());
                callableStatement.setInt(12, logInfoDto.getInfoLevel());
                callableStatement.setString(13, logInfoDto.getStepName());
                callableStatement.setString(14, logInfoDto.getContentType());
                callableStatement.setString(15, logInfoDto.getServerName());
                callableStatement.setString(16, logInfoDto.getMainMsgErr());

                if (logInfoDto.getIibErrCode() == null) {
                    callableStatement.setNull(17, Types.NUMERIC);
                } else {
                    callableStatement.setInt(17, logInfoDto.getIibErrCode());

                }

                callableStatement.addBatch();
            }

            result = callableStatement.executeBatch();
            log.info(String.format("The number of rows inserted: %d", result.length));

            connection.commit();
        } catch (SQLException ex) {
            log.error("", ex);
            return 0;
        }
        return result.length;
    }
}
