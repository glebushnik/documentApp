package com.api.documentApp.domain.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EntityRequestMapper<D,E> {
    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);
}
