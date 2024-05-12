package com.api.documentApp.service.task.impl;

import com.api.documentApp.domain.DTO.document.DocumentRequestIdDTO;
import com.api.documentApp.domain.DTO.task.TaskRequestDTO;
import com.api.documentApp.domain.DTO.task.TaskRequestIdDTO;
import com.api.documentApp.domain.DTO.task.TaskResponseDTO;
import com.api.documentApp.domain.DTO.task.TaskUpdateRequestDTO;
import com.api.documentApp.domain.entity.TaskEntity;
import com.api.documentApp.domain.entity.UserEntity;
import com.api.documentApp.domain.enums.Role;
import com.api.documentApp.domain.mapper.task.TaskResponseMapper;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import com.api.documentApp.exception.task.TaskNotFoundByIdException;
import com.api.documentApp.exception.user.NotEnoughRightsException;
import com.api.documentApp.repo.document.DocumentRepo;
import com.api.documentApp.repo.task.TaskRepo;
import com.api.documentApp.repo.user.UserRepo;
import com.api.documentApp.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepo taskRepo;
    private final TaskResponseMapper taskResponseMapper;
    private final UserRepo userRepo;
    private final DocumentRepo documentRepo;
    @Override
    public TaskResponseDTO createTask(TaskRequestDTO requestDTO, String userNameFromAccess)
            throws DocumentNotFoundByIdException,
            UsernameNotFoundException
    {
        List<UserEntity> users = requestDTO.getUserEmails().stream()
                .map(email -> userRepo.findByEmail(email).orElseThrow(()
                        -> new UsernameNotFoundException(String.format("Пользователя с таким email : %s не существует", email))))
                .collect(Collectors.toList());
        var currentUser = userRepo.findByEmail(userNameFromAccess).get();
        var doc = documentRepo.findById(requestDTO.getDocId()).orElseThrow(
                ()-> new DocumentNotFoundByIdException(String.format("Документ с id : %s не найден.", requestDTO.getDocId()))
        );
        var task = TaskEntity.builder()
                .createdDate(requestDTO.getCreationDate())
                .creator(currentUser.getEmail())
                .deadline(requestDTO.getDeadline())
                .users(users)
                .description(requestDTO.getDescription())
                .document(doc)
                .header(requestDTO.getHeader())
                .status(requestDTO.getStatus())
                .build();

        users.forEach(user -> {
            user.getTasks().add(task);
        });
        userRepo.saveAll(users);
        return taskResponseMapper.toDto(taskRepo.save(task));
    }

    @Override
    public List<TaskResponseDTO> getAllTasks() {
        return taskResponseMapper.toDto(taskRepo.findAll());
    }

    @Override
    public TaskResponseDTO getTaskById(TaskRequestIdDTO requestIdDTO) throws TaskNotFoundByIdException, NotEnoughRightsException {
        var task = taskRepo.findById(requestIdDTO.getTaskId()).orElseThrow(
                ()->new TaskNotFoundByIdException(String.format("Задания с id : %d не найдено.", requestIdDTO.getTaskId()))
        );

        return taskResponseMapper.toDto(task);
    }

    @Override
    public List<TaskResponseDTO> getTasksByDoc(DocumentRequestIdDTO requestIdDTO, String userNameFromAccess) throws DocumentNotFoundByIdException, NotEnoughRightsException {
        var doc = documentRepo.findById(requestIdDTO.getDocId()).orElseThrow(
                ()->new DocumentNotFoundByIdException(String.format("Документ с id : %s не найден.", requestIdDTO.getDocId()))
        );
        var user = userRepo.findByEmail(userNameFromAccess).get();

        if(doc.getUser() == user || user.getRole() == Role.ADMIN) {
            return taskResponseMapper.toDto(taskRepo.findAllByDocument(doc));
        } else {
            throw new NotEnoughRightsException("Недостаточно прав.");
        }
    }

    @Override
    public void deleteTaskById(TaskRequestIdDTO requestIdDTO, String userNameFromAccess) throws TaskNotFoundByIdException, NotEnoughRightsException {
        var task = taskRepo.findById(requestIdDTO.getTaskId()).orElseThrow(
                ()-> new TaskNotFoundByIdException(String.format("Задание с таким id : %d не найдено.", requestIdDTO.getTaskId()))
        );
        var user = userRepo.findByEmail(userNameFromAccess).get();

        if(Objects.equals(userNameFromAccess, task.getCreator())){
            user.removeTask(task.getId());
            userRepo.save(user);
            taskRepo.delete(task);
        } else {
            throw new NotEnoughRightsException("Недостаточно прав.");
        }
    }

    @Override
    public TaskRequestDTO updateTaskById(TaskUpdateRequestDTO requestDTO) throws TaskNotFoundByIdException, NotEnoughRightsException {
        return null;
    }
}
