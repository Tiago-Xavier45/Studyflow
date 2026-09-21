package com.tiagoxavier.study_service.dto;

import jakarta.validation.constraints.NotBlank;

public class DisciplineRequest {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    private String description;

    private Long userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}