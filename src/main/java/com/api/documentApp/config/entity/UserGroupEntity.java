package com.api.documentApp.config.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.REFRESH,
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "user_document_group_mapping",
            joinColumns = @JoinColumn(name = "user_group_id"),
            inverseJoinColumns = @JoinColumn(name = "company_group_id")
    )
    @JsonIgnore
    private Set<DocumentGroupEntity> documentGroups = new HashSet<>();

    @OneToMany( mappedBy = "userGroup",
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    @JsonIgnore
    private List<UserEntity> users;
}
