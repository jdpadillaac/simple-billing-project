package com.app.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.app.enums.RolNombre;
import com.app.models.Rol;

public interface RolService {
    
    public Rol save(Rol rol);
    public List<Rol> findAll();
    public Optional<Rol> getByRolNombre(RolNombre rolNombre);

}