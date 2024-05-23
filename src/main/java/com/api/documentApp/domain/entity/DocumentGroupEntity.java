package com.api.documentApp.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "document_groups")
public class DocumentGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "documentGroups")
    @JsonIgnore
    private List<UserGroupEntity> userGroups = new ArrayList<>();

    @OneToMany(mappedBy = "documentGroup")
    private List<DocumentEntity> docs;

    @PreRemove
    private void removeDocGroupsFromUserGroups() {
        for (UserGroupEntity u : userGroups) {
            u.getDocumentGroups().remove(this);
        }
    }
}
