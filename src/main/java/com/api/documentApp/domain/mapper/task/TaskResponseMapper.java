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
    TaskResponseDTO toDto(TaskEntity entity);

    @Override
    List<TaskEntity> toEntity(List<TaskResponseDTO> dtoList);

    @Override
    List<TaskResponseDTO> toDto(List<TaskEntity> entityList);
}
