package com.api.documentApp.domain.DTO.usergroup;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupRequestDTO {
    private String name;
    private List<String> emails;
}
