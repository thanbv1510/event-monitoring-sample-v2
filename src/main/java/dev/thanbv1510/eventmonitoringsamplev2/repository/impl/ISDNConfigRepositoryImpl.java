package dev.thanbv1510.eventmonitoringsamplev2.repository.impl;

import dev.thanbv1510.eventmonitoringsamplev2.domain.dto.ISDNConfigDto;
import dev.thanbv1510.eventmonitoringsamplev2.repository.ISDNConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleTypes;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ISDNConfigRepositoryImpl implements ISDNConfigRepository {
    private final DataSource dataSource;

    @Override
    public List<ISDNConfigDto> getArrISDNConfig() {
        String sqlCommand = "{call PROC_GET_ISDN_CONFIG(?)}";
        ResultSet rs;
        try (Connection conn = dataSource.getConnection();
             CallableStatement cst = conn.prepareCall(sqlCommand)) {
            cst.registerOutParameter(1, OracleTypes.CURSOR);
            cst.execute();

            List<ISDNConfigDto> isdnConfigDtos = new ArrayList<>();
            rs = cst.getObject(1, ResultSet.class);
            if (rs != null) {
                while (rs.next()) {
                    String msgFlowName = rs.getString("msgflow_name");
                    String operation = rs.getString("operation");
                    String strBeginIdx = rs.getString("str_begin_idx");
                    String strEndIdx = rs.getString("str_end_idx");
                    int isdnLength = rs.getInt("isdn_length");

                    ISDNConfigDto isdnConfig = ISDNConfigDto.builder()
                            .msgFlowName(msgFlowName)
                            .operation(operation)
                            .strBeginIdx(strBeginIdx)
                            .strEndIdx(strEndIdx)
                            .isdnLength(isdnLength)
                            .build();

                    isdnConfigDtos.add(isdnConfig);
                }
            }

            return isdnConfigDtos;

        } catch (SQLException ex) {
            log.error("", ex);
        }

        return Collections.emptyList();
    }
}
