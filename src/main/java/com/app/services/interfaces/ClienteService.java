package com.app.services.interfaces;

import java.util.List;

import com.app.models.Cliente;

public interface ClienteService {
    
    public List<Cliente> findAll();

    public Cliente save(Cliente cliente);

    public void delete(Long id);

    public Cliente findById(Long id);
    
}