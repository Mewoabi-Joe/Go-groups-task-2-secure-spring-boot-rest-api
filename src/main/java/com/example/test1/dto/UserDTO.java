package com.example.test1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotBlank(message = "the username has to be provided")
    private String username;
    @Email
    @NotBlank(message = "the email has to be provided")
    private String email;
    @NotBlank(message = "the password has to be provided")
    private String password;

}
