package com.example.test1.responses;

import com.example.test1.models.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateResponse {

    private Long id;

    private String name;

    private String note;

    private String imageUrl;

}
