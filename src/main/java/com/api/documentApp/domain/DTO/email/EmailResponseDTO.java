package com.api.documentApp.domain.DTO.email;

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
public class EmailResponseDTO {
    private String emailReceiver;
    private List<String> docIds;
    private Instant sentTime;
}
