package com.api.documentApp.service.documentchange;

import com.api.documentApp.domain.DTO.documentchange.DocumentChangeRequestDTO;
import com.api.documentApp.domain.DTO.documentchange.DocumentChangeResponseDTO;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import com.api.documentApp.exception.documentchange.DocumentChangeNotFoundByIdException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DocumentChangeService {
    public DocumentChangeResponseDTO createDocumentChange(DocumentChangeRequestDTO requestDTO, String userNameFromAccess) throws DocumentNotFoundByIdException;
    public List<DocumentChangeResponseDTO> getAllDocumentChangesByDoc(String docId) throws DocumentNotFoundByIdException;
    public void deleteDocumentChangeById(Long documentChangeId) throws DocumentChangeNotFoundByIdException;
    public DocumentChangeResponseDTO getDocumentChangeById(Long documentChangeId) throws DocumentChangeNotFoundByIdException;
    public List<DocumentChangeResponseDTO> getAllDocumentChanges();
}
