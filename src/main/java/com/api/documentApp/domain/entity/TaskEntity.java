package com.api.documentApp.domain.entity;

import com.api.documentApp.domain.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String header;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private Instant createdDate;

    private Instant deadline;

    @ManyToMany(mappedBy = "tasks", fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JsonIgnore
    private Set<UserEntity> users = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "document")
    private DocumentEntity document;

}
