package com.api.documentApp.domain.DTO.document;

import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import com.api.documentApp.domain.enums.DocStatus;
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
public class DocumentRequestDTO {
    private String id;
    @Enumerated(EnumType.STRING)
    private DocStatus status;

    private List<String> relatedDocIds;

    private String parentDocId;

    private List<String> relatedUserGroupIds;

    private Long docGroupId;

    private Instant expirationDate;

    private String comment;
}
