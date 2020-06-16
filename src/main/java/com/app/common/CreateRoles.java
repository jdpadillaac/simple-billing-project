package com.app.common;

import com.app.enums.RolNombre;
import com.app.models.Rol;
import com.app.services.interfaces.RolService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CreateRoles implements CommandLineRunner{

    @Autowired
    RolService rolService;

    @Override
    public void run(String... args) throws Exception {

        Rol roladmin = new Rol(RolNombre.ROLE_ADMIN);
        Rol rolUser = new Rol(RolNombre.ROL_USER);

        rolService.save(roladmin);
        rolService.save(rolUser);

    }
    
}