package com.api.documentApp.domain.DTO.task;

import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.DTO.user.UserResponseDTO;
import com.api.documentApp.domain.enums.TaskStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private String header;
    private String description;
    private String creator;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    private Instant creationDate;
    private Instant deadline;
    private DocumentResponseDTO doc;
    private List<UserResponseDTO> users;
}
