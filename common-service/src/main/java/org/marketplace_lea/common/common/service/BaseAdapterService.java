package org.marketplace_lea.common.common.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class BaseAdapterService<DTO, ID> {
    public abstract DTO getById(ID id);

    public abstract List<DTO> findAll();

    public abstract DTO save(DTO dto);

    public DTO update(DTO dto) {
        return this.save(dto);
    }

    public abstract void deleteById(ID id);

    public abstract void delete(DTO dto);
}
