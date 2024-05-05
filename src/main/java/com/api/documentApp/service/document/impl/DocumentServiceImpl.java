package com.api.documentApp.service.document.impl;

import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.DTO.document.DocumentResponseMessage;
import com.api.documentApp.domain.entity.DocumentEntity;
import com.api.documentApp.domain.mapper.document.DocumentEntityDTOMapper;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import com.api.documentApp.repo.document.DocumentRepo;
import com.api.documentApp.service.document.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepo documentRepo;
    private final DocumentEntityDTOMapper documentEntityDTOMapper;

    @Override
    public DocumentResponseMessage storeDoc(MultipartFile file) throws IOException {
        var doc = documentRepo.save(
                DocumentEntity.builder()
                        .fileName(file.getOriginalFilename())
                        .type(file.getContentType())
                        .createdDate(Instant.now())
                        .data(file.getBytes())
                        .build()
        );
        return DocumentResponseMessage.builder()
                .id(doc.getId())
                .build();
    }


    @Override
    public DocumentEntity getDocById(String docId) throws DocumentNotFoundByIdException {
        var doc = documentRepo.findById(docId).orElseThrow(
                ()->new DocumentNotFoundByIdException(String.format("Документа с id : %s не найдено.", docId))
        );
        return doc;
    }

    @Override
    public List<DocumentResponseDTO> getAllDocs() {
        return documentEntityDTOMapper.toDto(documentRepo.findAll());
    }
}
