package com.api.documentApp.domain.entity;

import com.api.documentApp.domain.enums.DocStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documents")
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileType;

    private String fileName;

    private Timestamp createdDate;

    private byte[] grpData;

    @Enumerated(EnumType.STRING)
    private DocStatus status;

    @OneToMany( mappedBy = "document",
            fetch = FetchType.LAZY,
            cascade = CascadeType.DETACH
    )
    @JsonIgnore
    private List<DocumentChangeEntity> documentChanges;

    @OneToMany( mappedBy = "document",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    @JsonIgnore
    private List<TaskEntity> tasks;
}
