package com.api.documentApp.service.task;

import com.api.documentApp.domain.DTO.document.DocumentRequestIdDTO;
import com.api.documentApp.domain.DTO.task.*;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import com.api.documentApp.exception.task.TaskNotFoundByIdException;
import com.api.documentApp.exception.user.NotEnoughRightsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface TaskService {
    public TaskResponseDTO createTask(TaskRequestDTO requestDTO, String userNameFromAccess)
            throws DocumentNotFoundByIdException,
            UsernameNotFoundException;
    public List<TaskResponseDTO> getAllTasks();
    public TaskResponseDTO getTaskById(TaskRequestIdDTO requestIdDTO)
            throws TaskNotFoundByIdException,
            NotEnoughRightsException;
    public List<TaskResponseDTO> getTasksByDoc(DocumentRequestIdDTO requestIdDTO, String userNameFromAccess)
            throws DocumentNotFoundByIdException,
            NotEnoughRightsException;
    public void deleteTaskById(TaskRequestIdDTO requestIdDTO, String userNameFromAccess)
            throws TaskNotFoundByIdException,
            NotEnoughRightsException;

    public TaskResponseDTO updateTaskById(UpdateTaskRequestDTO requestDTO)
            throws TaskNotFoundByIdException,
            DocumentNotFoundByIdException,
            NotEnoughRightsException;

    public List<TaskResponseDTO> getCurrentUserTasks(String usernameFromAccess);
}
