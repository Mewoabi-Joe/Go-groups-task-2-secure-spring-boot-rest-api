package com.example.test1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDTO {

    @NotBlank(message = "name is required")
    private String name;

    private String note;

    private String imageUrl;
}
