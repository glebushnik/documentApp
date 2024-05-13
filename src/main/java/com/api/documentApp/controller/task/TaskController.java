package com.api.documentApp.controller.task;

import com.api.documentApp.domain.DTO.document.DocumentRequestIdDTO;
import com.api.documentApp.domain.DTO.task.TaskRequestDTO;
import com.api.documentApp.domain.DTO.task.TaskRequestIdDTO;
import com.api.documentApp.security.JwtService;
import com.api.documentApp.service.task.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
@SecurityRequirement(name = "Bearer Authentication")
public class TaskController {
    private final TaskService taskService;
    private final JwtService jwtService;

    @PostMapping("/new")
    public ResponseEntity<?> createNewTask(
            @RequestBody TaskRequestDTO requestDTO,
            HttpServletRequest request
    ) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            String usernameFromAccess = jwtService.extractUserName(token);
            return ResponseEntity.ok().body(taskService.createTask(requestDTO, usernameFromAccess));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllTasks(){
        try {
            return ResponseEntity.ok().body(taskService.getAllTasks());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long taskId) {
        try {
            var requestIdDTO = TaskRequestIdDTO.builder().taskId(taskId).build();
            return ResponseEntity.ok().body(taskService.getTaskById(requestIdDTO));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/doc/{docId}")
    public ResponseEntity<?> getTasksByDoc(
            @PathVariable String docId,
            HttpServletRequest request
    ) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            String usernameFromAccess = jwtService.extractUserName(token);
            var requestIdDTO = DocumentRequestIdDTO.builder().docId(docId).build();
            return ResponseEntity.ok().body(taskService.getTasksByDoc(requestIdDTO, usernameFromAccess));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTaskById(
            @PathVariable Long taskId,
            HttpServletRequest request
    ) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            String usernameFromAccess = jwtService.extractUserName(token);
            var requestIdDto = TaskRequestIdDTO.builder().taskId(taskId).build();
            taskService.deleteTaskById(requestIdDto, usernameFromAccess);
            return ResponseEntity.ok().body(String.format("Задание с id : %d удалено.",taskId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
