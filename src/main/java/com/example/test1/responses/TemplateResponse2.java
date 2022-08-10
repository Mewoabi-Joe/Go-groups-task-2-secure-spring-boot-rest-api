package com.example.test1.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateResponse2 {

    private Long id;

    private String name;

    private String note;

    private String imageUrl;

    private String category;

}
