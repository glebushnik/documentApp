package com.api.documentApp.domain.DTO.documentchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentChangeRequestDTO {
    private String documentId;

    private String header;

    private String message;

    private Timestamp changedDate;
}
