package com.app.services.implementations;

import javax.transaction.Transactional;

import com.app.dao.UsuarioDao;
import com.app.models.Usuario;
import com.app.services.interfaces.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    UsuarioDao usuarioDao;

    @Override
    public Usuario getByUserName(String userName) {
        return usuarioDao.findByUserName(userName);
    }

    public Usuario save(Usuario usuario) {
        return usuarioDao.save(usuario);
    }
    
}