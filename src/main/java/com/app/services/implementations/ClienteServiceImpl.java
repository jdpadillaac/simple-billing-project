package com.app.services.implementations;

import java.util.List;

import com.app.dao.ClienteDao;
import com.app.models.Cliente;
import com.app.services.interfaces.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl implements ClienteService {
    
    @Autowired
    private ClienteDao clientedao;

    @Transactional(readOnly = true)
    public List<Cliente> findAll() {
        return (List<Cliente>) clientedao.findAll();
    }
}