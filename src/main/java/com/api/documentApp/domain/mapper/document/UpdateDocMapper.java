package com.api.documentApp.domain.mapper.document;

import com.api.documentApp.domain.DTO.document.DocumentRequestDTO;
import com.api.documentApp.domain.entity.DocumentEntity;
import com.api.documentApp.domain.entity.UserGroupEntity;
import com.api.documentApp.repo.usergroup.UserGroupRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateDocMapper {
    private final UserGroupRepo userGroupRepo;
    public DocumentEntity updateDocument(DocumentRequestDTO requestDTO, DocumentEntity document) {
        document.setStatus(requestDTO.getStatus()!=null ? requestDTO.getStatus() : document.getStatus());
        document.setRelatedDocs(!requestDTO.getRelatedDocIds().isEmpty() ? requestDTO.getRelatedDocIds() : document.getRelatedDocs());
        document.setParentDocId(requestDTO.getParentDocId()!=null ? requestDTO.getParentDocId() : document.getParentDocId());
        document.setGroupIds(!requestDTO.getRelatedUserGroupIds().isEmpty() ? requestDTO.getRelatedUserGroupIds() : document.getGroupIds());
        document.setExpirationDate(requestDTO.getExpirationDate() != null ? requestDTO.getExpirationDate() : document.getExpirationDate());

        return document;
    }
}
