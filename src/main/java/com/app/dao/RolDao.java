package com.app.dao;

import java.util.Optional;

import com.app.enums.RolNombre;
import com.app.models.Rol;

import org.springframework.data.repository.CrudRepository;

public interface RolDao extends CrudRepository<Rol, Long> {

    Optional<Rol> findByRolNombre(RolNombre rolNombre);

}