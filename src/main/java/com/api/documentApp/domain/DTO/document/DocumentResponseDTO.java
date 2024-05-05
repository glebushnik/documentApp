package com.api.documentApp.domain.DTO.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponseDTO {
    private String name;
    private String url;
    private String type;
    private long size;
}
