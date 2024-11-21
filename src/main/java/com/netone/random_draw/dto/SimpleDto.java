package com.netone.random_draw.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter @NoArgsConstructor
public class SimpleDto {
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String ACTIVE_STATUS = "activeStatus";

    private long id;

    @NotEmpty(message = "Name is required")
    private String name;

    public SimpleDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

}
