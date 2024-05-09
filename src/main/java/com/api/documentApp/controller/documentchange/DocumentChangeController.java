package com.api.documentApp.controller.documentchange;

import com.api.documentApp.domain.DTO.documentchange.DocumentChangeRequestDTO;
import com.api.documentApp.domain.DTO.documentchange.DocumentChangeResponseDTO;
import com.api.documentApp.security.JwtService;
import com.api.documentApp.service.documentchange.DocumentChangeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/doc-changes")
@SecurityRequirement(name = "Bearer Authentication")
public class DocumentChangeController {
    private final DocumentChangeService documentChangeService;
    private final JwtService jwtService;


    @PostMapping("/new")
    @Operation(
            summary = "Create Document Change",
            description = "Create a new document change. The request body should be a DocumentChangeRequestDTO object. Authorization header with JWT token is required.",
            tags = { "document changes", "post" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = DocumentChangeResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
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
    @Operation(
            summary = "Get Changes by Document ID",
            description = "Get all document changes by document ID.",
            tags = { "document changes", "get" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = DocumentChangeResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    public ResponseEntity<?> getChangesByDocId(@PathVariable String docId) {
        try {
            return ResponseEntity.ok().body(documentChangeService.getAllDocumentChangesByDoc(docId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{docChangeId}")
    @Operation(
            summary = "Delete Document Change by ID",
            description = "Delete a document change by its ID.",
            tags = { "document changes", "delete" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(mediaType = "text/plain") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    public ResponseEntity<?> deleteDocumentChangeById(@PathVariable Long docChangeId) {
        try {
            documentChangeService.deleteDocumentChangeById(docChangeId);
            return ResponseEntity.ok().body(String.format("Изменение документа c id : %d удалено.", docChangeId));
        } catch (Exception e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{docChangeId}")
    @Operation(
            summary = "Get Document Change by ID",
            description = "Get a document change by its ID.",
            tags = { "document changes", "get" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = DocumentChangeResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    public ResponseEntity<?> getDocumentChangeById(@PathVariable Long docChangeId) {
        try {
            return ResponseEntity.ok().body(documentChangeService.getDocumentChangeById(docChangeId));
        } catch (Exception e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get All Document Changes",
            description = "Get all document changes.",
            tags = { "document changes", "get" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = DocumentChangeResponseDTO.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    public ResponseEntity<?> getAllDocumentChanges() {
        try {
            return ResponseEntity.ok().body(documentChangeService.getAllDocumentChanges());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
