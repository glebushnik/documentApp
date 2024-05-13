package com.api.documentApp.domain.mapper.document;

import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.entity.DocumentEntity;
import com.api.documentApp.domain.mapper.EntityResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentResponseMapper extends EntityResponseMapper<DocumentResponseDTO, DocumentEntity> {
    @Override

    DocumentEntity toEntity(DocumentResponseDTO dto);

    @Override
    @Mapping(target = "owner", source = "entity.user.username")
    @Mapping(target = "userGroups", source = "entity.groupIds")
    @Mapping(target = "url", source = "entity.url")
    @Mapping(target = "expirationDate", source= "entity.expirationDate")
    DocumentResponseDTO toDto(DocumentEntity entity);

    @Override
    List<DocumentEntity> toEntity(List<DocumentResponseDTO> dtoList);

    @Override
    List<DocumentResponseDTO> toDto(List<DocumentEntity> entityList);
}
