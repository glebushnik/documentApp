package com.api.documentApp.controller.document;

import com.api.documentApp.domain.entity.DocumentEntity;
import com.api.documentApp.service.document.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/docs")
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadDoc(
            @RequestParam("file")MultipartFile file
    ) {
        try {
            return ResponseEntity.ok().body(
              documentService.storeDoc(file)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    String.format("Документ %s не загружен.", file.getOriginalFilename())
            );
        }
    }

    @GetMapping("/{docId}")
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
}

