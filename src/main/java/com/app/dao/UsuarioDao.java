package com.app.dao;

import com.app.models.Usuario;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioDao extends CrudRepository<Usuario, Long> {
    

    // Tipo consulta Implemementacion de una convencion de nombres 
    // public Usuario findByUsernameAndEmail(String username);

    // Anotacion query de JPA
    @Query("select u from Usuario u where u.username=?1 ")
    public Usuario findByUserName(String username);
}