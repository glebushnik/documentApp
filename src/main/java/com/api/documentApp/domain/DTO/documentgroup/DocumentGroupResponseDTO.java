package com.api.documentApp.domain.DTO.documentgroup;

import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentGroupResponseDTO {
    private Long id;
    private String name;
    private List<DocumentResponseDTO> documents;
    private List<UserGroupResponseDTO> userGroups;
}
