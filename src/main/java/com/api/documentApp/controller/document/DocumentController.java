package com.api.documentApp.controller.document;

import com.api.documentApp.domain.DTO.document.DocumentRequestDTO;
import com.api.documentApp.domain.DTO.document.DocumentResponseMessage;
import com.api.documentApp.domain.entity.DocumentEntity;
import com.api.documentApp.security.JwtService;
import com.api.documentApp.service.document.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/docs")
@SecurityRequirement(name = "Bearer Authentication")
public class DocumentController {
    private final DocumentService documentService;
    private final JwtService jwtService;

    @PostMapping("/upload")
    @Operation(
            summary = "Upload Document",
            description = "Upload a document.",
            tags = { "documents", "upload"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = DocumentResponseMessage.class), mediaType = "document id") }),
            @ApiResponse(responseCode = "417", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> uploadDoc(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            String usernameFromAccess = jwtService.extractUserName(token);
            return ResponseEntity.ok().body(documentService.storeDoc(file, usernameFromAccess));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    String.format("Документ %s не загружен.", file.getOriginalFilename())
            );
        }
    }

    @GetMapping("/{docId}")
    @Operation(
            summary = "Get/download Document by Id",
            description = "Retrieve a document by its id.",
            tags = { "documents", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = byte[].class), mediaType = "application/octet-stream") }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> getDocById(@PathVariable String docId) {
        try {
            DocumentEntity doc = documentService.getDocById(docId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFileName() + "\"")
                    .body(doc.getData());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{docId}")
    @Operation(
            summary = "Delete Document by Id",
            description = "Delete a document by its id.",
            tags = { "documents", "delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public ResponseEntity<?> deleteDocById(
            @PathVariable String docId,
            HttpServletRequest request
    ) {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            String usernameFromAccess = jwtService.extractUserName(token);
            documentService.deleteDocById(docId, usernameFromAccess);
            return ResponseEntity.ok().body(
                    String.format("Документ с id : %s успешно удален.", docId)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

