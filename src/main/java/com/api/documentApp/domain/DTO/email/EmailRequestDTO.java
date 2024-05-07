package com.api.documentApp.domain.DTO.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestDTO {
    private Long receiverId;
    private String docId;
    private String header;
    private String body;
}
