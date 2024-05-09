package com.api.documentApp.service.documentchange.impl;

import com.api.documentApp.domain.DTO.documentchange.DocumentChangeRequestDTO;
import com.api.documentApp.domain.DTO.documentchange.DocumentChangeResponseDTO;
import com.api.documentApp.domain.entity.DocumentChangeEntity;
import com.api.documentApp.domain.mapper.documentchange.DocumentChangeResponseMapper;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import com.api.documentApp.exception.documentchange.DocumentChangeNotFoundByIdException;
import com.api.documentApp.repo.document.DocumentRepo;
import com.api.documentApp.repo.documentchange.DocumentChangeRepo;
import com.api.documentApp.service.documentchange.DocumentChangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentChangeServiceImpl implements DocumentChangeService {
    private final DocumentChangeRepo documentChangeRepo;
    private final DocumentRepo documentRepo;
    private final DocumentChangeResponseMapper documentChangeResponseMapper;
    @Override
    public DocumentChangeResponseDTO createDocumentChange(DocumentChangeRequestDTO requestDTO, String userNameFromAccess) throws DocumentNotFoundByIdException {
        var doc = documentRepo.findById(requestDTO.getDocumentId()).orElseThrow(
                () -> new DocumentNotFoundByIdException(String.format("Документ с id : %s не найден.", requestDTO.getDocumentId()))
        );

        var docChange = DocumentChangeEntity.builder()
                .changedDate(requestDTO.getChangedDate())
                .document(doc)
                .header(requestDTO.getHeader())
                .message(requestDTO.getMessage())
                .createdBy(userNameFromAccess)
                .build();

        var docChanges = doc.getDocumentChanges();

        docChanges.add(documentChangeRepo.save(docChange));

        doc.setDocumentChanges(docChanges);

        documentRepo.save(doc);

        return documentChangeResponseMapper.toDto(docChange);
    }

    @Override
    public List<DocumentChangeResponseDTO> getAllDocumentChangesByDoc(String docID) throws DocumentNotFoundByIdException {
        var doc = documentRepo.findById(docID).orElseThrow(
                () -> new DocumentNotFoundByIdException(String.format("Документ с id : %s не найден.", docID))
        );

        return documentChangeResponseMapper.toDto(doc.getDocumentChanges());
    }

    @Override
    public void deleteDocumentChangeById(Long documentChangeId) throws DocumentChangeNotFoundByIdException {
        var docChange = documentChangeRepo.findById(documentChangeId).orElseThrow(
                () -> new DocumentChangeNotFoundByIdException(String.format("Изменение документа с id : %d не найдено" ,documentChangeId))
        );

        var doc = docChange.getDocument();
        var docChanges = doc.getDocumentChanges();
        docChanges.remove(docChange);
        doc.setDocumentChanges(docChanges);
        documentRepo.save(doc);

        documentChangeRepo.delete(docChange);
    }

    @Override
    public DocumentChangeResponseDTO getDocumentChangeById(Long documentChangeId) throws DocumentChangeNotFoundByIdException {
        var docChange = documentChangeRepo.findById(documentChangeId).orElseThrow(
                ()-> new DocumentChangeNotFoundByIdException(String.format("Изменение документа с id : %d не найдено.", documentChangeId))
        );

        return documentChangeResponseMapper.toDto(docChange);
    }

    @Override
    public List<DocumentChangeResponseDTO> getAllDocumentChanges() {
        return documentChangeResponseMapper.toDto(documentChangeRepo.findAll());
    }
}
