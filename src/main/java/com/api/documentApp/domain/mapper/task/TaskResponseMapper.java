package com.api.documentApp.domain.mapper.task;

import com.api.documentApp.domain.DTO.task.TaskResponseDTO;
import com.api.documentApp.domain.entity.TaskEntity;
import com.api.documentApp.domain.mapper.EntityResponseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskResponseMapper extends EntityResponseMapper<TaskResponseDTO, TaskEntity> {
    @Override
    TaskEntity toEntity(TaskResponseDTO dto);

    @Override
    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "doc", source = "entity.document")
    @Mapping(target = "users", source = "entity.users")
    @Mapping(target = "creationDate", source = "entity.createdDate")
    TaskResponseDTO toDto(TaskEntity entity);

    @Override
    List<TaskEntity> toEntity(List<TaskResponseDTO> dtoList);

    @Override
    @Mapping(target = "id", source = "entity.id")
    @Mapping(target = "doc", source = "entity.document")
    @Mapping(target = "users", source = "entity.users")
    @Mapping(target = "creationDate", source = "entity.createdDate")
    List<TaskResponseDTO> toDto(List<TaskEntity> entityList);
}
