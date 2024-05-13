package com.api.documentApp.domain.DTO.document;

import com.api.documentApp.domain.enums.DocStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponseDTO {
    private String id;
    private String fileName;
    private String url;
    private String type;
    private String owner;
    private Instant createdDate;
    private Instant expirationDate;
    private String parentDocId;
    @Enumerated(EnumType.STRING)
    private DocStatus status;
    private List<String> relatedDocs;
    private List<String> userGroups;
    private String comment;
    private long size;
}
