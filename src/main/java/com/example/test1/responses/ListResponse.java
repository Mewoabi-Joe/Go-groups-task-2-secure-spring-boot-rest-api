package com.example.test1.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListResponse {
    private Long   id;
    private String name;
    private LocalDateTime createdDate;
    private String state;
}
