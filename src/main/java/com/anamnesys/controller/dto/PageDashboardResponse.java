package com.anamnesys.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class PageDashboardResponse {
    private List<InfoDashboard> content;
    private int totalPages;
    private long totalElements;
    private int number;
}
