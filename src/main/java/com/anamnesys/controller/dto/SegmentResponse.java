package com.anamnesys.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SegmentResponse {

    private Long id;
    private String name;
    private String description;

}
