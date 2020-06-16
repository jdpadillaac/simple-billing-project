package com.app.services.interfaces;

import com.app.models.Usuario;

public interface UsuarioService  {

    public Usuario getByUserName(String userName);

    public Usuario save(Usuario usuario);

}
    
