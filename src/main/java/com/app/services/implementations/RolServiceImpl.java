package com.app.services.implementations;

import java.util.List;
import java.util.Optional;

import com.app.dao.RolDao;
import com.app.enums.RolNombre;
import com.app.models.Rol;
import com.app.services.interfaces.RolService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    RolDao rolDao;

    @Override
    public Rol save(Rol rol) {
        return rolDao.save(rol);
    }

    @Override
    public List<Rol> findAll() {
        return (List<Rol>) rolDao.findAll();
    }

    public Optional<Rol> getByRolNombre(RolNombre rolNombre) {
        return rolDao.findByRolNombre(rolNombre);
    }
    
}