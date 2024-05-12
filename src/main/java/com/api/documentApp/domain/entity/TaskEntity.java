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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private String creator;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private Instant createdDate;

    private Instant deadline;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "tasks")
    @JsonIgnore
    private List<UserEntity> users = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "document")
    private DocumentEntity document;

    @PreRemove
    private void removeTasksFromUsers() {
        for (UserEntity u : users) {
            u.getTasks().remove(this);
        }
    }
}
