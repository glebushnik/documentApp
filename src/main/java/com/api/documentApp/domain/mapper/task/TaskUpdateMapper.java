package com.api.documentApp.domain.mapper.task;

import com.api.documentApp.domain.DTO.task.UpdateTaskRequestDTO;
import com.api.documentApp.domain.entity.TaskEntity;
import com.api.documentApp.domain.entity.UserEntity;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import com.api.documentApp.repo.document.DocumentRepo;
import com.api.documentApp.repo.task.TaskRepo;
import com.api.documentApp.repo.user.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TaskUpdateMapper {
    private final TaskRepo taskRepo;
    private final DocumentRepo documentRepo;
    private final UserRepo userRepo;
    public TaskEntity mapTaskEntityWithDTO(TaskEntity task, UpdateTaskRequestDTO requestDTO) {
        var document = documentRepo.findById(requestDTO.getDocId());
        List<UserEntity> users = requestDTO.getUserEmails().stream()
                .map(email -> userRepo.findByEmail(email).get())
                .collect(Collectors.toList());
        var oldUsers = task.getUsers();
        task.setHeader(requestDTO.getHeader() != null ? requestDTO.getHeader() : task.getHeader());
        task.setDescription(requestDTO.getDescription() != null ? requestDTO.getDescription() : task.getDescription());
        task.setStatus(requestDTO.getStatus() != null ? requestDTO.getStatus() : task.getStatus());
        task.setCreatedDate(requestDTO.getCreationDate() != null ? requestDTO.getCreationDate() : task.getCreatedDate());
        task.setDeadline(requestDTO.getDeadline() != null ? requestDTO.getDeadline() : task.getDeadline());
        task.setDocument(document != null ? document.get() : task.getDocument());

        if(!users.isEmpty()) {
            oldUsers.forEach(user -> {
                user.removeTask(task);
            });
            task.setUsers(users);
            users.forEach(user -> {
                user.getTasks().add(task);
            });
            taskRepo.save(task);
        }


        return task;
    }
}
