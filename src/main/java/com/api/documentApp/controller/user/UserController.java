package com.api.documentApp.controller.user;

import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.DTO.documentgroup.DocumentGroupResponseDTO;
import com.api.documentApp.domain.DTO.user.UserResponseDTO;
import com.api.documentApp.domain.entity.UserEntity;
import com.api.documentApp.exception.user.UserNotFoundByIdException;
import com.api.documentApp.security.JwtService;
import com.api.documentApp.service.document.DocumentService;
import com.api.documentApp.service.documentgroup.DocumentGroupService;
import com.api.documentApp.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final DocumentGroupService documentGroupService;
    private final DocumentService documentService;
    @GetMapping("/{userId}")
    @Operation(
            summary = "Retrieve User by Id",
            description = "Get a User object by specifying its id. The response is User object with id, name, email, password, createdAt, updatedAt and role.",
            tags = { "users", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(userService.getUserById(userId));
        } catch (UserNotFoundByIdException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/docs")
    @Operation(
            summary = "Get User Documents by User Id",
            description = "Retrieve documents belonging to a specific user identified by user id.",
            tags = { "users", "get", "documents"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = DocumentResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> getUserDocsById(
            @PathVariable Long userId,
            HttpServletRequest request
    ) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            String usernameFromAccess = jwtService.extractUserName(token);
            return ResponseEntity.ok().body(userService.getUserDocuments(userId, usernameFromAccess));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/current")
    @Operation(
            summary = "Get Current User Info",
            description = "Retrieve information about the currently logged-in user.",
            tags = { "users", "get", "auth" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    public ResponseEntity<?> getCurrentUserInfo(HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            String usernameFromAccess = jwtService.extractUserName(token);
            return ResponseEntity.ok().body(userService.getCurrentUser(usernameFromAccess));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/current-group-members")
    @Operation(
            summary = "Get Current User Group Members",
            description = "Retrieve the list of group members associated with the currently logged-in user.",
            tags = { "users", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = List.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    public ResponseEntity<?> getCurrentUserGroupMembers(HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            String usernameFromAccess = jwtService.extractUserName(token);
            return ResponseEntity.ok().body(userService.getCurrentUserGroupMembers(usernameFromAccess));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/document-groups/all")
    @Operation(
            summary = "Get All Document Groups",
            description = "Retrieve a list of all document groups.",
            tags = { "users", "document-groups", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Document groups retrieved successfully", content = @Content(schema = @Schema(implementation = DocumentGroupResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<?> getAllDocumentGroups(){
        try {
            return ResponseEntity.ok().body(documentGroupService.getAllDocumentGroups());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/document-groups/{documentGroupId}")
    @Operation(
            summary = "Get Document Group by ID",
            description = "Retrieve a document group by its ID.",
            tags = { "users","document-groups", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Document group retrieved successfully", content = @Content(schema = @Schema(implementation = DocumentGroupResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<?> getDocumentGroupById(@PathVariable Long documentGroupId) {
        try {
            return ResponseEntity.ok().body(documentGroupService.getDocumentGroupById(documentGroupId));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/document/get-info/{docId}")
    @Operation(
            summary = "Get Document Info by ID",
            description = "Retrieve a document Info by its ID.",
            tags = { "users","documents", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Document info retrieved successfully", content = @Content(schema = @Schema(implementation = DocumentGroupResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<?> getDocumentInfoById(@PathVariable String docId, HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            String usernameFromAccess = jwtService.extractUserName(token);
            return ResponseEntity.ok().body(documentService.getDocInfoById(docId, usernameFromAccess));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    @Operation(
            summary = "Retrieve All Users",
            description = "Get a list of all users. The response is a list of UserResponseDTO objects with id, name, email, password, createdAt, updatedAt and role.",
            tags = {"users", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserResponseDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponseDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/docs/add-user/{userId}/{docId}")
    @Operation(
            summary = "Add privated user to document users",
            description = "Add privated user to document user list providing its Id and docId.",
            tags = {"users", "documents", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Document group updated successfully", content = @Content(schema = @Schema(implementation = DocumentGroupResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<?> addPrivatedUser(@PathVariable Long userId, @PathVariable String docId) {
        try {
            documentService.addPrivatedUser(docId, userId);
            return ResponseEntity.ok().body(String.format("Пользователь с id : %d добавлен в список пользователей документа : %s.", userId, docId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/docs/remove-user/{userId}/{docId}")
    @Operation(
            summary = "Remove privated user to document users",
            description = "Remove privated user to document user list providing its Id and docId.",
            tags = {"users", "documents", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Document group updated successfully", content = @Content(schema = @Schema(implementation = DocumentGroupResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<?> removePrivatedUser(@PathVariable Long userId, @PathVariable String docId) {
        try {
            documentService.removePrivatedUser(docId, userId);
            return ResponseEntity.ok().body(String.format("Пользователь с id : %d удален из списка пользователей документа : %s.", userId, docId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
