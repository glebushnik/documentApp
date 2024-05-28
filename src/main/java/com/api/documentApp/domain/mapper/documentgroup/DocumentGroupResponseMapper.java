package com.api.documentApp.domain.mapper.documentgroup;

import com.api.documentApp.domain.DTO.documentgroup.DocumentGroupResponseDTO;
import com.api.documentApp.domain.entity.DocumentEntity;
import com.api.documentApp.domain.entity.DocumentGroupEntity;
import com.api.documentApp.domain.entity.UserGroupEntity;
import com.api.documentApp.domain.mapper.EntityResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DocumentGroupResponseMapper extends EntityResponseMapper<DocumentGroupResponseDTO, DocumentGroupEntity> {
    @Override
    DocumentGroupEntity toEntity(DocumentGroupResponseDTO dto);

    @Mapping(target = "docs", source = "entity.docs")
    DocumentGroupResponseDTO toDto(DocumentGroupEntity entity);

    @Override
    List<DocumentGroupEntity> toEntity(List<DocumentGroupResponseDTO> dtoList);

    @Override
    List<DocumentGroupResponseDTO> toDto(List<DocumentGroupEntity> entityList);
}
