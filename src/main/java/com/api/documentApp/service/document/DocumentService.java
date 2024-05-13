package com.api.documentApp.service.document;

import com.api.documentApp.domain.DTO.document.DocumentRequestDTO;
import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.DTO.document.DocumentResponseMessage;
import com.api.documentApp.domain.DTO.user.UserDocRequestDTO;
import com.api.documentApp.domain.DTO.user.UserResponseDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import com.api.documentApp.domain.entity.DocumentEntity;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import com.api.documentApp.exception.user.NotEnoughRightsException;
import com.api.documentApp.exception.user.UserNotFoundByIdException;
import com.api.documentApp.exception.usergroup.UserGroupNotFoundByIdException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {
    public DocumentResponseMessage storeDoc(MultipartFile file, String userNameFromAccess) throws IOException;
    public DocumentResponseDTO setProperties(DocumentRequestDTO requestDTO, String userNameFromAccess)
            throws DocumentNotFoundByIdException,
            UserGroupNotFoundByIdException,
            NotEnoughRightsException;
    public DocumentEntity getDocById(String fileName) throws DocumentNotFoundByIdException;

    public List<DocumentResponseDTO> getAllDocs();

    public void deleteDocById(String docId, String userName)
            throws DocumentNotFoundByIdException,
            NotEnoughRightsException;

    public List<DocumentResponseDTO> getGroupDocs(String userNameFromAccess);

}
