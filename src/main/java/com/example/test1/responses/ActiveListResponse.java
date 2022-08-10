package com.example.test1.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveListResponse {
    private Long list_id;
    private String list_name;
    private String state;
    private List<ItemsByCategoryResponse> items_by_category;
}
