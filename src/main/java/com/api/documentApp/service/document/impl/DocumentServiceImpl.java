package com.api.documentApp.service.document.impl;

import com.api.documentApp.domain.DTO.document.DocumentRequestDTO;
import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.DTO.document.DocumentResponseMessage;
import com.api.documentApp.domain.entity.DocumentEntity;
import com.api.documentApp.domain.entity.DocumentGroupEntity;
import com.api.documentApp.domain.entity.UserEntity;
import com.api.documentApp.domain.entity.UserGroupEntity;
import com.api.documentApp.domain.enums.Role;
import com.api.documentApp.domain.mapper.document.DocumentResponseMapper;
import com.api.documentApp.domain.mapper.document.UpdateDocMapper;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import com.api.documentApp.exception.documentgroup.DocumentGroupNotFoundByIdException;
import com.api.documentApp.exception.user.NotEnoughRightsException;
import com.api.documentApp.exception.user.UserNotFoundByIdException;
import com.api.documentApp.exception.usergroup.UserGroupNotFoundByIdException;
import com.api.documentApp.repo.document.DocumentRepo;
import com.api.documentApp.repo.documentgroup.DocumentGroupRepo;
import com.api.documentApp.repo.user.UserRepo;
import com.api.documentApp.repo.usergroup.UserGroupRepo;
import com.api.documentApp.service.document.DocumentService;
import com.ibm.icu.text.Transliterator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepo documentRepo;
    private final UserRepo userRepo;
    private final UpdateDocMapper updateDocMapper;
    private final DocumentResponseMapper documentResponseMapper;
    private final UserGroupRepo userGroupRepo;
    private final DocumentGroupRepo documentGroupRepo;

    @Override
    public DocumentResponseMessage storeDoc(
            MultipartFile file,
            String userNameFromAccess
    ) throws IOException{
        var user = userRepo.findByEmail(userNameFromAccess).get();
        boolean containsCyrillic = file.getOriginalFilename().codePoints()
                .mapToObj(Character.UnicodeScript::of)
                .anyMatch(Character.UnicodeScript.CYRILLIC::equals);
        Transliterator toLatin = Transliterator.getInstance("Cyrillic-Latin");
        var fileName = toLatin.transliterate(file.getOriginalFilename());
        List<UserEntity> users = new ArrayList<>();
        users.add(user);
        var doc = documentRepo.save(
                DocumentEntity.builder()
                        .fileName(containsCyrillic ? fileName : file.getOriginalFilename())
                        .type(file.getContentType())
                        .createdDate(Instant.now())
                        .owner(user.getEmail())
                        .users(users)
                        .data(file.getBytes())
                        .groupIds(user.getUserGroups().stream().map(UserGroupEntity::getId).map(String::valueOf).collect(Collectors.toList()))
                        .build()
        );
        doc.setUrl(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/docs/")
                        .path(doc.getId())
                        .toUriString()
        );
        documentRepo.save(doc);
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
       return documentResponseMapper.toDto(documentRepo.findAll());
    }

    @Override
    public void deleteDocById(String docId, String userName)
            throws DocumentNotFoundByIdException,
            NotEnoughRightsException {
        var doc = documentRepo.findById(docId).orElseThrow(
                () -> new DocumentNotFoundByIdException(String.format("Документ с id : %s не найден.", docId))
        );
        if(doc.getDocumentGroup()!=null) {
            var documentGroup = doc.getDocumentGroup();
            documentGroup.getDocs().remove(doc);
        }
        var reqUser = userRepo.findByEmail(userName).get();
        var docUser = userRepo.findByEmail(doc.getOwner()).get();

        var users = doc.getUsers();
        if (reqUser.getRole() == Role.ADMIN || reqUser == docUser) {
            users.forEach(
                    user -> {
                        user.removeDoc(doc);
                    }
            );
            userRepo.saveAll(users);
            documentRepo.delete(doc);
        } else {
            throw new NotEnoughRightsException("Вы не являетесь владельцем этого файла.");
        }
    }

    @Override
    public DocumentResponseDTO setProperties(DocumentRequestDTO requestDTO, String userNameFromAccess) throws DocumentNotFoundByIdException, UserGroupNotFoundByIdException, DocumentGroupNotFoundByIdException,NotEnoughRightsException {
        var user = userRepo.findByEmail(userNameFromAccess).get();
        var doc = documentRepo.findById(requestDTO.getId()).orElseThrow(
                ()->new DocumentNotFoundByIdException(String.format("Документ с id : %s не найден", requestDTO.getId()))
        );
        if(doc.getDocumentGroup() != null) {
            var docGroup = documentGroupRepo.findById(requestDTO.getDocGroupId()).orElseThrow(
                    () -> new DocumentGroupNotFoundByIdException(String.format("Группа документов с id : %d не найдена.", requestDTO.getDocGroupId()))
            );
            docGroup.getDocs().remove(doc);
        }
        var docUser = userRepo.findByEmail(doc.getOwner()).get();
        if(user.getRole() == Role.ADMIN || docUser == user) {
            if (documentRepo.findAllById(requestDTO.getRelatedDocIds()).contains(null)) {
                throw new DocumentNotFoundByIdException("Документов с такими id не найдено.");
            }
            if (userGroupRepo.findAllById(requestDTO.getRelatedUserGroupIds().stream().map(Long::parseLong).collect(Collectors.toList())).contains(null)) {
                throw new UserGroupNotFoundByIdException("Групп с такими id не найдено.");
            }
            documentRepo.save(updateDocMapper.updateDocument(requestDTO, doc));
            return documentResponseMapper.toDto(doc);
        } else {
            throw new NotEnoughRightsException("Недостаточно прав.");
        }
    }

    @Override
    public List<DocumentResponseDTO> getGroupDocs(String userNameFromAccess)
    {
        var reqUser = userRepo.findByEmail(userNameFromAccess).get();

        if(reqUser.getRole() == Role.ADMIN) {
            return documentResponseMapper.toDto(documentRepo.findAll());
        } else {
            var groups = reqUser.getUserGroups();
            List<DocumentEntity> groupDocs = groups.stream()
                    .flatMap(userGroup -> userGroup.getDocumentGroups().stream())
                    .flatMap(documentGroup -> documentGroup.getDocs().stream())
                    .collect(Collectors.toList());

            return documentResponseMapper.toDto(groupDocs);
        }
    }

    @Override
    public void addPrivatedUser(String docId, Long userId) throws DocumentNotFoundByIdException, UserNotFoundByIdException {
        var doc = documentRepo.findById(docId).orElseThrow(
                ()->new DocumentNotFoundByIdException(String.format("Документ с id : %s не найден.", docId))
        );

        var user = userRepo.findById(userId).orElseThrow(
                ()->new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден.", userId))
        );

        doc.getUsers().add(user);
        documentRepo.save(doc);
        user.getDocs().add(doc);
        userRepo.save(user);
    }

    @Override
    public void removePrivatedUser(String docId, Long userId) throws DocumentNotFoundByIdException, UserNotFoundByIdException {
        var doc = documentRepo.findById(docId).orElseThrow(
                ()->new DocumentNotFoundByIdException(String.format("Документ с id : %s не найден.", docId))
        );

        var user = userRepo.findById(userId).orElseThrow(
                ()->new UserNotFoundByIdException(String.format("Пользователь с id : %d не найден.", userId))
        );

        user.removeDoc(doc);
        userRepo.save(user);
    }
}
