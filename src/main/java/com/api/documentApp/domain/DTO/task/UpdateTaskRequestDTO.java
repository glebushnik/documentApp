package com.api.documentApp.domain.DTO.task;

import com.api.documentApp.domain.enums.TaskStatus;
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
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskRequestDTO {
    private Long id;
    private String header;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    private Instant creationDate;
    private Instant deadline;
    private String docId;
    private List<String> userEmails;
}
