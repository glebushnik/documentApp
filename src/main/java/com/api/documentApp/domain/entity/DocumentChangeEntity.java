package com.api.documentApp.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "document_changes")
public class DocumentChangeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String header;

    private String message;

    private Timestamp changedDate;

    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "document")
    private DocumentEntity document;
}
