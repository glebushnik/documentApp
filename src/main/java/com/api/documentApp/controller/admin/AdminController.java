package com.api.documentApp.controller.admin;

import com.api.documentApp.domain.DTO.user.UserRequestDTO;
import com.api.documentApp.domain.DTO.user.UserResponseDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupRequestDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import com.api.documentApp.exception.user.UserNotFoundByIdException;
import com.api.documentApp.exception.usergroup.UserGroupNotFoundByIdException;
import com.api.documentApp.service.user.UserService;
import com.api.documentApp.service.usergroup.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final UserGroupService userGroupService;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
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
    public ResponseEntity<?> getUserGroup(@PathVariable Long userId){
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
    public ResponseEntity<?> deleteUserGroupById(@PathVariable Long userGroupId) {
        try {
            userGroupService.deleteUserGroupById(userGroupId);
            return ResponseEntity.ok(String.format("Группа пользователей с id : %d удалена.", userGroupId));
        } catch (UserGroupNotFoundByIdException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usergroups/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllUserGroups() {
        try {
            List<UserGroupResponseDTO> userGroupResponseDTOS = userGroupService.getAllUserGroups();
            return ResponseEntity.ok(userGroupResponseDTOS);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUserById(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok().body(String.format("Пользователь с id : %d удален", userId));
        } catch (UserNotFoundByIdException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateUserById(@PathVariable Long userId, @RequestBody UserRequestDTO requestDTO) {
        try {
            return ResponseEntity.ok().body(userService.updateUserById(userId, requestDTO));
        } catch (UserNotFoundByIdException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
