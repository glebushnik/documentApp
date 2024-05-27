package com.api.documentApp.service.documentgroup.impl;

import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.DTO.documentgroup.DocumentGroupRequestDTO;
import com.api.documentApp.domain.DTO.documentgroup.DocumentGroupResponseDTO;
import com.api.documentApp.domain.DTO.usergroup.UserGroupResponseDTO;
import com.api.documentApp.domain.entity.DocumentGroupEntity;
import com.api.documentApp.domain.entity.UserEntity;
import com.api.documentApp.domain.entity.UserGroupEntity;
import com.api.documentApp.domain.enums.Role;
import com.api.documentApp.domain.mapper.document.DocumentResponseMapper;
import com.api.documentApp.domain.mapper.documentgroup.DocumentGroupResponseMapper;
import com.api.documentApp.domain.mapper.usergroup.UserGroupResponseMapper;
import com.api.documentApp.exception.document.DocumentNotFoundByIdException;
import com.api.documentApp.exception.documentgroup.DocumentGroupNotFoundByIdException;
import com.api.documentApp.exception.user.NotEnoughRightsException;
import com.api.documentApp.exception.usergroup.UserGroupNotFoundByIdException;
import com.api.documentApp.repo.document.DocumentRepo;
import com.api.documentApp.repo.documentgroup.DocumentGroupRepo;
import com.api.documentApp.repo.user.UserRepo;
import com.api.documentApp.repo.usergroup.UserGroupRepo;
import com.api.documentApp.service.documentgroup.DocumentGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DocumentGroupServiceImpl implements DocumentGroupService {
    private final DocumentGroupRepo documentGroupRepo;
    private final UserGroupRepo userGroupRepo;
    private final DocumentRepo documentRepo;
    private final UserRepo userRepo;
    private final DocumentGroupResponseMapper documentGroupResponseMapper;
    private final DocumentResponseMapper documentResponseMapper;
    private final UserGroupResponseMapper userGroupResponseMapper;
    @Override
    public DocumentGroupResponseDTO createDocumentGroup(DocumentGroupRequestDTO requestDTO) throws DocumentNotFoundByIdException, UserGroupNotFoundByIdException {
        var userGroups = requestDTO.getUserGroupIds().stream()
                .map(id -> {
                    try {
                        return userGroupRepo.findById(id)
                                .orElseThrow(() -> new UserGroupNotFoundByIdException(
                                        String.format("Группа пользователей с id : %d не найдена", id)));
                    } catch (UserGroupNotFoundByIdException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        var docs = requestDTO.getDocIds().stream()
                .map(id -> {
                    try {
                        return documentRepo.findById(id).orElseThrow(
                                ()-> new DocumentNotFoundByIdException(String.format("Документ с id : %s не найден.", id))
                        );
                    } catch (DocumentNotFoundByIdException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        var documentGroup = DocumentGroupEntity.builder()
                .userGroups(userGroups)
                .docs(docs)
                .name(requestDTO.getName())
                .build();
        documentGroupRepo.save(documentGroup);
        docs.forEach(
                document -> {
                    document.setDocumentGroup(documentGroup);
                }
        );
        docs.forEach(
                document -> {
                    document.getUsers().addAll(
                            userGroups.stream()
                                    .flatMap(userGroup -> userGroup.getUsers().stream())
                                    .collect(Collectors.toList())
                    );
                }

        );
        userGroups.forEach(
                group -> {
                    group.getDocumentGroups().add(documentGroup);
                }
        );
        userGroupRepo.saveAll(userGroups);
        return documentGroupResponseMapper.toDto(documentGroup);
    }

    @Override
    public List<DocumentGroupResponseDTO> getAllDocumentGroups() {
        return documentGroupResponseMapper.toDto(documentGroupRepo.findAll());
    }

    @Override
    public DocumentGroupResponseDTO getDocumentGroupById(Long documentGroupId) throws DocumentGroupNotFoundByIdException {
        var documentGroup = documentGroupRepo.findById(documentGroupId).orElseThrow(
                ()-> new DocumentGroupNotFoundByIdException(String.format("Группа документов с id : %d не найдена", documentGroupId))
        );

        return documentGroupResponseMapper.toDto(documentGroup);
    }

    @Override
    public void deleteDocumentGroupById(Long documentGroupId) throws DocumentGroupNotFoundByIdException {
        var documentGroup = documentGroupRepo.findById(documentGroupId).orElseThrow(
                ()-> new DocumentGroupNotFoundByIdException(String.format("Группа документов с id : %d не найдена", documentGroupId))
        );


        var docs = documentGroup.getDocs();

        docs.forEach(
                document -> {
                    document.setDocumentGroup(null);
                }
        );
        documentGroupRepo.delete(documentGroup);
    }

    @Override
    public DocumentGroupResponseDTO updateDocumentGroupById(Long documentGroupId, DocumentGroupRequestDTO requestDTO) throws DocumentGroupNotFoundByIdException, DocumentNotFoundByIdException, UserGroupNotFoundByIdException {
        var userGroups = requestDTO.getUserGroupIds().stream()
                .map(id -> {
                    try {
                        return userGroupRepo.findById(id)
                                .orElseThrow(() -> new UserGroupNotFoundByIdException(
                                        String.format("Группа пользователей с id : %d не найдена", id)));
                    } catch (UserGroupNotFoundByIdException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        var docs = requestDTO.getDocIds().stream()
                .map(id -> {
                    try {
                        return documentRepo.findById(id).orElseThrow(
                                ()-> new DocumentNotFoundByIdException(String.format("Документ с id : %s не найден.", id))
                        );
                    } catch (DocumentNotFoundByIdException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        var documentGroup = documentGroupRepo.findById(documentGroupId).orElseThrow(
                ()-> new DocumentGroupNotFoundByIdException(String.format("Группа документов с id : %d не найдена.", documentGroupId))
        );

        var entityGroups = documentGroup.getUserGroups();
        var entityDocs = documentGroup.getDocs();

        entityGroups.forEach(
                group -> {
                    group.getDocumentGroups().remove(documentGroup);
                }
        );
        entityDocs.forEach(
                document -> {
                    document.setDocumentGroup(null);
                }
        );

        documentGroup.setUserGroups(userGroups);
        documentGroup.setDocs(docs);

        docs.forEach(
                document -> {
                    document.setDocumentGroup(documentGroup);
                }
        );
        userGroups.forEach(
                group -> {
                    group.getDocumentGroups().add(documentGroup);
                }
        );

        documentGroup.setName(requestDTO.getName()!=null ? requestDTO.getName() : documentGroup.getName());
        return documentGroupResponseMapper.toDto(documentGroupRepo.save(documentGroup));
    }

    @Override
    public List<DocumentResponseDTO> getDocumentsByDocumentGroupId(Long documentGroupId, String userNameFromAccess)
            throws DocumentGroupNotFoundByIdException,
            NotEnoughRightsException {
        var documentGroup = documentGroupRepo.findById(documentGroupId).orElseThrow(
                ()-> new DocumentGroupNotFoundByIdException(String.format("Группа документов с id : %d не найдена.", documentGroupId))
        );
        var user = userRepo.findByEmail(userNameFromAccess).get();

        if(user.getRole() == Role.ADMIN) {
            return documentResponseMapper.toDto(documentRepo.findAll());
        }

        boolean isDocumentGroupPresent = user.getUserGroups().stream()
                .anyMatch(group -> group.getDocumentGroups().contains(documentGroup));

        if(isDocumentGroupPresent) return documentResponseMapper.toDto(documentGroup.getDocs());
        else throw new NotEnoughRightsException("Недостаточно прав.");
    }

    @Override
    public List<UserGroupResponseDTO> getUserGroupsByDocumentGroup(Long documentGroupId) throws DocumentGroupNotFoundByIdException {
        var documentGroup = documentGroupRepo.findById(documentGroupId).orElseThrow(
                ()-> new DocumentGroupNotFoundByIdException(String.format("Группа документов с id : %d не найдена.", documentGroupId))
        );

        return userGroupResponseMapper.toDto(documentGroup.getUserGroups());
    }
}
