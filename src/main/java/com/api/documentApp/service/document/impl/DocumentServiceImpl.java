package com.api.documentApp.service.document.impl;

import com.api.documentApp.domain.DTO.document.DocumentRequestDTO;
import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.DTO.document.DocumentResponseMessage;
import com.api.documentApp.domain.DTO.user.UserDocRequestDTO;
import com.api.documentApp.domain.entity.DocumentEntity;
import com.api.documentApp.domain.enums.Role;
import com.api.documentApp.domain.mapper.document.DocumentEntityDTOMapper;
import com.api.documentApp.domain.mapper.document.DocumentResponseMapper;
import com.api.documentApp.domain.mapper.document.UpdateDocMapper;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import com.api.documentApp.exception.user.NotEnoughRightsException;
import com.api.documentApp.exception.user.UserNotFoundByIdException;
import com.api.documentApp.exception.usergroup.UserGroupNotFoundByIdException;
import com.api.documentApp.repo.document.DocumentRepo;
import com.api.documentApp.repo.user.UserRepo;
import com.api.documentApp.repo.usergroup.UserGroupRepo;
import com.api.documentApp.service.document.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepo documentRepo;
    private final DocumentEntityDTOMapper documentEntityDTOMapper;
    private final UserRepo userRepo;
    private final UpdateDocMapper updateDocMapper;
    private final DocumentResponseMapper documentResponseMapper;

    @Override
    public DocumentResponseMessage storeDoc(
            MultipartFile file,
            String userNameFromAccess
    ) throws IOException{
        var user = userRepo.findByEmail(userNameFromAccess).get();
        var doc = documentRepo.save(
                DocumentEntity.builder()
                        .fileName(file.getOriginalFilename())
                        .type(file.getContentType())
                        .createdDate(Instant.now())
                        .user(user)
                        .data(file.getBytes())
                        .build()
        );
        var userDocs = user.getDocs();
        userDocs.add(doc);
        user.setDocs(userDocs);
        userRepo.save(user);
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
       return documentRepo.findAll().stream()
                .map(documentResponseMapper::toDto)
                .peek(documentResponseDTO -> {
                    String fileDownloadUrl = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/api/docs/")
                            .path(documentResponseDTO.getId())
                            .toUriString();
                    documentResponseDTO.setUrl(fileDownloadUrl);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDocById(String docId, String userName)
            throws DocumentNotFoundByIdException,
            NotEnoughRightsException {
        var doc = documentRepo.findById(docId).orElseThrow(
                () -> new DocumentNotFoundByIdException(String.format("Документ с id : %s не найден.", docId))
        );
        var reqUser = userRepo.findByEmail(userName).get();
        var docUser = doc.getUser();
        var userDocs = docUser.getDocs();
        if(!userDocs.isEmpty()) {
            if (reqUser.getRole() == Role.ADMIN || reqUser == docUser) {
                userDocs.remove(doc);
                docUser.setDocs(userDocs);
                userRepo.save(docUser);
                documentRepo.delete(doc);
            } else {
                throw new NotEnoughRightsException("Вы не являетесь владельцем этого файла.");
            }
        }
    }

    @Override
    public DocumentResponseDTO setProperties(DocumentRequestDTO requestDTO) throws DocumentNotFoundByIdException, UserGroupNotFoundByIdException {
        var doc = documentRepo.findById(requestDTO.getId()).get();
        documentRepo.save(updateDocMapper.updateDocument(requestDTO,doc));
        DocumentResponseDTO documentResponseDTO = documentResponseMapper.toDto(doc);
        String fileDownloadUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/docs/")
                .path(documentResponseDTO.getId())
                .toUriString();
        documentResponseDTO.setUrl(fileDownloadUrl);
        return documentResponseDTO;
    }
}
