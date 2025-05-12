package com.app.coinkeeper.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private String color;
    private String icon;
}