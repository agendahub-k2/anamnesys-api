package com.anamnesys.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TermResponse {
    private Long id;
    private String name;
    private String term;
}
