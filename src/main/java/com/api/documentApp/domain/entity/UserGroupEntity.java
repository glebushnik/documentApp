package com.api.documentApp.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_groups")
public class UserGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "userGroups")
    @JsonIgnore
    private List<UserEntity> users = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "user_doc_groups",
            joinColumns = { @JoinColumn(name = "group_id") },
            inverseJoinColumns = { @JoinColumn(name = "docgroup_id") })
    private List<DocumentGroupEntity> documentGroups = new ArrayList<>();

    @PreRemove
    private void removeGroupsFromUsers() {
        for (UserEntity u : users) {
            u.getUserGroups().remove(this);
        }
    }

    public void addDocumentGroup(DocumentGroupEntity docGroup) {
        this.documentGroups.add(docGroup);
        docGroup.getUserGroups().add(this);
    }

    public void removeDocumentGroup(DocumentGroupEntity docGroup) {
        var docGroups = this.getDocumentGroups();
        docGroups.remove(docGroup);
        this.setDocumentGroups(docGroups);
    }
}
