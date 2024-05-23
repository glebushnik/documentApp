package com.api.documentApp.domain.DTO.documentgroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentGroupRequestDTO {
    private String name;
    private List<String> docIds;
    private List<Long> userGroupIds;
}
