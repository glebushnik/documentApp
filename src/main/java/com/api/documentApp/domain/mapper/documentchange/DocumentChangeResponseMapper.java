package com.api.documentApp.domain.mapper.documentchange;

import com.api.documentApp.domain.DTO.documentchange.DocumentChangeResponseDTO;
import com.api.documentApp.domain.entity.DocumentChangeEntity;
import com.api.documentApp.domain.mapper.EntityResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentChangeResponseMapper extends EntityResponseMapper <DocumentChangeResponseDTO, DocumentChangeEntity> {
    @Override
    DocumentChangeEntity toEntity(DocumentChangeResponseDTO dto);

    @Override
    @Mapping(target = "documentId", source = "entity.document.id")
    DocumentChangeResponseDTO toDto(DocumentChangeEntity entity);

    @Override
    List<DocumentChangeEntity> toEntity(List<DocumentChangeResponseDTO> dtoList);

    @Override
    List<DocumentChangeResponseDTO> toDto(List<DocumentChangeEntity> entityList);
}
