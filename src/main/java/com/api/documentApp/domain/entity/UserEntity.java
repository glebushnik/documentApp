package com.api.documentApp.domain.entity;

import com.api.documentApp.domain.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "jwt_users")
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String patronymic;

    private String post;

    private String department;

    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;


    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "user_usergroups",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "group_id") })
    private List<UserGroupEntity> userGroups = new ArrayList<>();

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "user_tasks",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    @JsonIgnore
    private List<TaskEntity> tasks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<DocumentEntity> docs;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void addUserGroup(UserGroupEntity group) {
        this.userGroups.add(group);
        group.getUsers().add(this);
    }

    public void removeUserGroup(UserGroupEntity userGroup) {
        var userGroups = this.getUserGroups();
        userGroups.remove(userGroup);
        this.setUserGroups(userGroups);
    }

    public void addTask(TaskEntity task) {
        this.tasks.add(task);
        task.getUsers().add(this);
    }

    public void removeTask(TaskEntity task) {
        var userTasks = this.getTasks();
        userTasks.remove(task);
        this.setTasks(userTasks);
    }
}
