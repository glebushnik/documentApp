package com.api.documentApp.domain.mapper.document;

import com.api.documentApp.domain.DTO.document.DocumentResponseDTO;
import com.api.documentApp.domain.entity.DocumentEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentEntityDTOMapper {
    public List<DocumentResponseDTO> toDto(List<DocumentEntity> docs) {
        List<DocumentResponseDTO> responseDocs = docs.stream().map(
                doc -> {
                    String fileDownloadUrl = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/api/docs/")
                            .path(doc.getId())
                            .toUriString();
                    return DocumentResponseDTO.builder()
                            .id(doc.getId())
                            .fileName(doc.getFileName())
                            .type(doc.getType())
                            .owner(doc.getUser().getEmail())
                            .size(doc.getData().length)
                            .url(fileDownloadUrl)
                            .build();
                }
        ).collect(Collectors.toList());
        return responseDocs;
    }
}
