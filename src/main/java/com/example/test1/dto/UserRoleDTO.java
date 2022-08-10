package com.example.test1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDTO {

    @NotBlank(message = "the username has to be provided")
    private String userName;

    @NotBlank(message = "the rolename has to be provided")
    private String roleName;

}
