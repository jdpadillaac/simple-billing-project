package com.app.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RegionCreateDto {
    
    @NotEmpty
    @Size(min = 5, max = 15)
    private String name;

    @NotEmpty
    private String code;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}