package com.app.controllers;

import java.util.List;

import com.app.models.Cliente;
import com.app.services.interfaces.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class ClienteRestController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("clientes")
    public List<Cliente> index() {
        return clienteService.findAll();
    }


}