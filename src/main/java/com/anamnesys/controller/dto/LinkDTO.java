package com.anamnesys.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LinkDTO {
    private String url;

    public LinkDTO(String url) {
        this.url = url;
    }
}
