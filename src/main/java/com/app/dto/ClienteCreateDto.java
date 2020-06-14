package com.app.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class ClienteCreateDto {
    
    @NotNull
    private String nombre;

    @NotNull
    private String apellido;

    @Email
    private String email;

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
		this.email = email;
	}

}

