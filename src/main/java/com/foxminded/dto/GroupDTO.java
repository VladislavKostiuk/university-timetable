package com.foxminded.dto;

public record GroupDTO (
        Long id,
        String name
) {
    public GroupDTO(String name) {
        this(0L, name);
    }
}
