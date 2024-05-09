package com.api.documentApp.domain.mapper.document;

import com.api.documentApp.domain.DTO.document.DocumentRequestDTO;
import com.api.documentApp.domain.entity.DocumentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateDocMapper {
    public DocumentEntity updateDocument(DocumentRequestDTO requestDTO, DocumentEntity document) {
        document.setStatus(requestDTO.getStatus()!=null ? requestDTO.getStatus() : document.getStatus());
        document.setRelatedDocs(!requestDTO.getRelatedDocIds().isEmpty() ? requestDTO.getRelatedDocIds() : document.getRelatedDocs());
        document.setParentDocId(requestDTO.getParentDocId()!=null ? requestDTO.getParentDocId() : document.getParentDocId());
        document.setGroupIds(!requestDTO.getRelatedUserGroupIds().isEmpty() ? requestDTO.getRelatedUserGroupIds() : document.getGroupIds());
        document.setExpirationDate(requestDTO.getExpirationDate() != null ? requestDTO.getExpirationDate() : document.getExpirationDate());
        document.setComment(requestDTO.getComment()!=null ? requestDTO.getComment() : document.getComment());
        return document;
    }
}