package com.api.documentApp.controller.documentchange;

import com.api.documentApp.domain.DTO.documentchange.DocumentChangeRequestDTO;
import com.api.documentApp.security.JwtService;
import com.api.documentApp.service.documentchange.DocumentChangeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/doc-changes")
@SecurityRequirement(name = "Bearer Authentication")
public class DocumentChangeController {
    private final DocumentChangeService documentChangeService;
    private final JwtService jwtService;


    @PostMapping("/new")
    public ResponseEntity<?> createDocumentChange(@RequestBody DocumentChangeRequestDTO requestDTO, HttpServletRequest request) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            String usernameFromAccess = jwtService.extractUserName(token);
            return ResponseEntity.ok().body(documentChangeService.createDocumentChange(requestDTO, usernameFromAccess));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/doc/{docId}")
    public ResponseEntity<?> getChangesByDocId(@PathVariable String docId) {
        try {
            return ResponseEntity.ok().body(documentChangeService.getAllDocumentChangesByDoc(docId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{docChangeId}")
    public ResponseEntity<?> deleteDocumentChangeById(@PathVariable Long docChangeId) {
        try {
            documentChangeService.deleteDocumentChangeById(docChangeId);
            return ResponseEntity.ok().body(String.format("Изменение документа c id : %d удалено.", docChangeId));
        } catch (Exception e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{docChangeId}")
    public ResponseEntity<?> getDocumentChangeById(@PathVariable Long docChangeId) {
        try {
            return ResponseEntity.ok().body(documentChangeService.getDocumentChangeById(docChangeId));
        } catch (Exception e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDocumentChanges() {
        try {
            return ResponseEntity.ok().body(documentChangeService.getAllDocumentChanges());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
