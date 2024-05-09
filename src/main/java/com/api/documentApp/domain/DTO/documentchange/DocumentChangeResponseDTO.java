package com.api.documentApp.domain.DTO.documentchange;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentChangeResponseDTO {
    private String id;

    private String documentId;

    private String header;

    private String message;

    private Timestamp changedDate;

    private String createdBy;
}
