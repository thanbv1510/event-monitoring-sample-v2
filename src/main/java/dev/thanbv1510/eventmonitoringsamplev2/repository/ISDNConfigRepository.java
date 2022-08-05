package dev.thanbv1510.eventmonitoringsamplev2.repository;

import dev.thanbv1510.eventmonitoringsamplev2.domain.dto.ISDNConfigDto;

import java.util.List;

public interface ISDNConfigRepository {
    List<ISDNConfigDto> getArrISDNConfig();
}
