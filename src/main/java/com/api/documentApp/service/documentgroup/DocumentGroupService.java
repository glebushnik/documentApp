package com.api.documentApp.service.documentgroup;

import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.DTO.documentgroup.DocumentGroupRequestDTO;
import com.api.documentApp.domain.DTO.documentgroup.DocumentGroupResponseDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import com.api.documentApp.domain.entity.DocumentGroupEntity;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import com.api.documentApp.exception.documentgroup.DocumentGroupNotFoundByIdException;
import com.api.documentApp.exception.user.NotEnoughRightsException;
import com.api.documentApp.exception.usergroup.UserGroupNotFoundByIdException;

import java.util.List;

public interface DocumentGroupService {
    public DocumentGroupResponseDTO createDocumentGroup(DocumentGroupRequestDTO requestDTO)
        throws DocumentNotFoundByIdException,
            UserGroupNotFoundByIdException;

    public List<DocumentGroupEntity> getAllDocumentGroups();

    public DocumentGroupResponseDTO getDocumentGroupById(Long documentGroupId)
        throws DocumentGroupNotFoundByIdException;

    public void deleteDocumentGroupById(Long documentGroupId)
        throws DocumentGroupNotFoundByIdException;

    public DocumentGroupResponseDTO updateDocumentGroupById(Long documentGroupId, DocumentGroupRequestDTO requestDTO)
        throws DocumentGroupNotFoundByIdException,
            DocumentNotFoundByIdException,
            UserGroupNotFoundByIdException;


    public List<DocumentResponseDTO> getDocumentsByDocumentGroupId(Long documentGroupId, String userNameFromAccess)
        throws DocumentGroupNotFoundByIdException,
            NotEnoughRightsException;

    public List<UserGroupResponseDTO> getUserGroupsByDocumentGroup(Long documentGroupid)
        throws DocumentGroupNotFoundByIdException;

}
