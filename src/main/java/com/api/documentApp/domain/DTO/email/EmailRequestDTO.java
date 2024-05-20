package com.api.documentApp.domain.DTO.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestDTO {
    private String email;
    private List<String> docIds;
    private String header;
    private String body;
}
