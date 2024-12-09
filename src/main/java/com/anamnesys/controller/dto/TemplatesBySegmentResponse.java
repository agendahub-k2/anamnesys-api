package com.anamnesys.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class TemplatesBySegmentResponse {

    private Map<String, List<TemplateResponse>> templates;

}
