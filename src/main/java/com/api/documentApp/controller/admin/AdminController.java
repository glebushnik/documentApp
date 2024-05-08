package com.api.documentApp.controller.admin;

import com.api.documentApp.domain.DTO.document.DocumentRequestDTO;
import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.DTO.user.UserEmailRequestDTO;
import com.api.documentApp.domain.DTO.user.UserRequestDTO;
import com.api.documentApp.domain.DTO.user.UserResponseDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupRequestDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import com.api.documentApp.exception.user.UserNotFoundByEmailException;
import com.api.documentApp.exception.user.UserNotFoundByIdException;
import com.api.documentApp.exception.usergroup.UserGroupContainsNoSuchUsersException;
import com.api.documentApp.exception.usergroup.UserGroupNotFoundByIdException;
import com.api.documentApp.service.document.DocumentService;
import com.api.documentApp.service.user.UserService;
import com.api.documentApp.service.usergroup.UserGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {
    private final UserService userService;
    private final UserGroupService userGroupService;
    private final DocumentService documentService;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Retrieve All Users",
            description = "Get a list of all users. The response is a list of UserResponseDTO objects with id, name, email, password, createdAt, updatedAt and role.",
            tags = {"admin","users", "get"})
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

    @PostMapping("/usergroups/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Create a UserGroup",
            description = "Create a new user group. The request body should be a UserGroupRequestDTO object with name. The response is a UserGroupResponseDTO object with id, name and users.",
            tags = {"admin","user groups", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserGroupResponseDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<?> createUserGroup(@RequestBody UserGroupRequestDTO userGroupRequestDTO) {
        try {
            return ResponseEntity.ok(userGroupService.createUserGroup(userGroupRequestDTO));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/users/{userId}/group")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Retrieve UserGroup by UserId",
            description = "Get the user group associated with a user by specifying the user id. The response is a UserGroupResponseDTO object with id, name and users.",
            tags = {"admin","user groups", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserGroupResponseDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<?> getUserGroup(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(userService.getUserGroup(userId));
        } catch (UserNotFoundByIdException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usergroups/{userGroupId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Retrieve UserGroup by Id",
            description = "Get a user group object by specifying its id. The response is a UserGroupResponseDTO object with id, name and users.",
            tags = {"admin","user groups", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = UserGroupResponseDTO.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<?> getUserGroupById(@PathVariable Long userGroupId) {
        try {
            return ResponseEntity.ok(userGroupService.getUserGroupById(userGroupId));
        } catch (UserGroupNotFoundByIdException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/usergroups/{userGroupId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Delete UserGroup by Id",
            description = "Delete a user group by specifying its id. The response is a string indicating the success of the operation.",
            tags = {"admin", "user groups", "delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "500", content = {@Content(schema = @Schema())})})
    public ResponseEntity<String> deleteUserGroupById(@PathVariable Long userGroupId) {
        try {
            userGroupService.deleteUserGroupById(userGroupId);
            return ResponseEntity.ok(String.format("User group with id : %d deleted.", userGroupId));
        } catch (UserGroupNotFoundByIdException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/usergroups/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Retrieve All User Groups",
            description = "Get a list of all user groups. The response is a list of UserGroupResponseDTO objects with id, name and users.",
            tags = {"admin", "user groups", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserGroupResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> getAllUserGroups() {
        try {
            List<UserGroupResponseDTO> userGroupResponseDTOS = userGroupService.getAllUserGroups();
            return ResponseEntity.ok().body(userGroupResponseDTOS);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Delete User by Id",
            description = "Delete a user by specifying its id. The response is a string indicating the success of the operation.",
            tags = {"admin", "users", "delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok().body(String.format("User with id : %d deleted.", userId));
        } catch (UserNotFoundByIdException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Update User by Id",
            description = "Update a user by specifying its id. The request body should be a UserRequestDTO object with name, email, password, usergroupid and role. The response is a UserResponseDTO object with id, name, email, password, createdAt, updatedAt and role.",
            tags = { "admin", "users", "put"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> updateUserById(@PathVariable Long userId, @RequestBody UserRequestDTO requestDTO) {
        try {
            return ResponseEntity.ok().body(userService.updateUserById(userId, requestDTO));
        } catch (UserNotFoundByIdException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/usergroups/{userGroupId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Add User to User Group by Id",
            description = "Add a user to a user group by specifying the user group id. The request body should be a UserEmailRequestDTO object with email. The response is a UserGroupResponseDTO object with id, name and users.",
            tags = { "admin", "user groups", "put" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = UserGroupResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> addUserToUserGroupById(
            @PathVariable Long userGroupId,
            @RequestBody @Valid UserEmailRequestDTO requestDTO
    ) {
        try {
            return ResponseEntity.ok().body(
                    userGroupService.addUserToGroup(userGroupId, requestDTO)
            );
        } catch (UserNotFoundByEmailException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserGroupNotFoundByIdException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/docs/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Get All Documents",
            description = "Retrieve all documents.",
            tags = { "admin", "documents", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = DocumentResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> getAllDocs() {
        try {
            return ResponseEntity.ok().body(documentService.getAllDocs());
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}