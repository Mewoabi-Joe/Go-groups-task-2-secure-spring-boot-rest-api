package com.example.test1.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplatesByCategoryResponse {
    private String name;
    private List<TemplateResponse> templates = new ArrayList<TemplateResponse>();
}
