package com.example.test1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {

    @NotBlank(message = "the rolename has to be provided")
    private String roleName;

}
