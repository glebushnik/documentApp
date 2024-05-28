package com.api.documentApp.domain.DTO.documentgroup;

import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import com.api.documentApp.domain.entity.DocumentEntity;
import com.api.documentApp.domain.entity.UserGroupEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @JsonIgnoreProperties({"documentGroup", "data"})
    private List<DocumentEntity> docs;

    @JsonIgnoreProperties("documentGroups")
    private List<UserGroupEntity> userGroups;
}
