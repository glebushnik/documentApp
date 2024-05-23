package com.api.documentApp.domain.entity;

import com.api.documentApp.domain.enums.DocStatus;
import com.api.documentApp.domain.mapper.document.DocumentIdsToStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documents")


public class DocumentEntity {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String fileName;

    private String type;

    private Instant createdDate;

    private Instant expirationDate;

    private String parentDocId;

    private String url;

    @Convert(converter = DocumentIdsToStringConverter.class)
    private List<String> relatedDocs;

    @Lob
    private byte[] data;

    @Enumerated(EnumType.STRING)
    private DocStatus status;

    private String comment;

    @Convert(converter = DocumentIdsToStringConverter.class)
    private List<String> groupIds;

    @OneToMany( mappedBy = "document",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE
    )
    @JsonIgnore
    private List<DocumentChangeEntity> documentChanges;

    @OneToMany( mappedBy = "document",
            fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TaskEntity> tasks;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "documentgroup_id")
    private DocumentGroupEntity documentGroup;

}
