package com.api.documentApp.service.document;

import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.DTO.document.DocumentResponseMessage;
import com.api.documentApp.domain.entity.DocumentEntity;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {
    public DocumentResponseMessage storeDoc(MultipartFile file) throws IOException;

    public DocumentEntity getDocById(String fileName) throws DocumentNotFoundByIdException;

    public List<DocumentResponseDTO> getAllDocs();
}
