package com.api.documentApp.domain.DTO.task;

import com.api.documentApp.domain.enums.TaskStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateRequestDTO {
    private String header;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    private Instant creationDate;
    private Instant deadline;
}
