package com.api.documentApp.controller.task;

import com.api.documentApp.domain.DTO.document.DocumentRequestIdDTO;
import com.api.documentApp.domain.DTO.task.TaskRequestDTO;
import com.api.documentApp.domain.DTO.task.TaskRequestIdDTO;
import com.api.documentApp.domain.DTO.task.TaskResponseDTO;
import com.api.documentApp.security.JwtService;
import com.api.documentApp.service.task.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "Create New Task",
            description = "Create a new task with the provided details.",
            tags = { "tasks", "create", "auth" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TaskResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    })
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
    @Operation(
            summary = "Get All Tasks",
            description = "Retrieve all tasks.",
            tags = { "tasks", "get", "admin" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TaskResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllTasks(){
        try {
            return ResponseEntity.ok().body(taskService.getAllTasks());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{taskId}")
    @Operation(
            summary = "Get Task by ID",
            description = "Retrieve task details by its ID.",
            tags = { "tasks", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TaskResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    })
    public ResponseEntity<?> getTaskById(@PathVariable Long taskId) {
        try {
            var requestIdDTO = TaskRequestIdDTO.builder().taskId(taskId).build();
            return ResponseEntity.ok().body(taskService.getTaskById(requestIdDTO));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/doc/{docId}")
    @Operation(
            summary = "Get Tasks by Document",
            description = "Retrieve tasks associated with a specific document.",
            tags = { "tasks", "get", "documents" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TaskResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    })
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
    @Operation(
            summary = "Delete Task by ID",
            description = "Delete a task by its ID.",
            tags = { "tasks", "delete", "auth" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    })
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

    @GetMapping("/current-user-tasks")
    @Operation(
            summary = "Get Current User Tasks",
            description = "Retrieve tasks associated with the currently logged-in user.",
            tags = { "tasks", "get", "auth" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = TaskResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
    })
    public ResponseEntity<?> getCurrentUserTasks(HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            String usernameFromAccess = jwtService.extractUserName(token);
            return ResponseEntity.ok().body(taskService.getCurrentUserTasks(usernameFromAccess));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
