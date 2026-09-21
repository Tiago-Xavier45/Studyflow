package com.tiagoxavier.study_service.dto;

import java.time.LocalDateTime;

public class DisciplineResponse {

    private Long id;
    private String name;
    private String description;
    private Long userId;
    private LocalDateTime createdAt;

    public DisciplineResponse(
            Long id, String name, String description, Long userId, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}