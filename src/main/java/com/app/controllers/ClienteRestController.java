package com.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.models.Cliente;
import com.app.models.JsonResp;
import com.app.services.interfaces.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

// @CrossOrigin(origins = {}, methods = {} )
@RestController()
@RequestMapping("/api")
public class ClienteRestController {

    public JsonResp resp;

    public ClienteRestController() {
        resp = new JsonResp();
    }

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/clientes")
    @ResponseStatus(HttpStatus.OK)
    public List<Cliente> index() {
        return clienteService.findAll();
    }

    @RequestMapping("cliente/{id}")
    public ResponseEntity<JsonResp> show(@PathVariable Long id) {
    
        Cliente cliente = null;

        try {
            cliente = clienteService.findById(id);

            Map<String, Object> response = new HashMap<>();
            if (cliente == null) {
                response.put("errorDetail", "No existe cliente con el ID: ".concat(id.toString().concat(" No existe en la base de datos")));

                resp.success = false;
                resp.message = "Error en en la base de datos al consular cliente con id " + id;
                resp.error = response;
                return new ResponseEntity<JsonResp>(resp, HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException e) {
            resp.success = false;
            resp.message = "Error en en la base de datos al consular cliente con id " + id;
            resp.data = e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage());
            return new ResponseEntity<JsonResp>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        resp.success = true;
        resp.message = "Cliente cargado correctamente";
        resp.data = cliente;
        return new ResponseEntity<JsonResp>(resp, HttpStatus.OK);
    }

    @PostMapping("/clientes")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente create(@RequestBody Cliente cliente) {
        return clienteService.save(cliente);
    }

    @PutMapping("/cliente/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente update(@RequestBody Cliente cliente, @PathVariable long id) {
        Cliente clienteActual = clienteService.findById(id);
        clienteActual.setApellidos(cliente.getApellidos());
        clienteActual.setNombre(cliente.getNombre());
        clienteActual.setEmail(cliente.getEmail());
        return clienteService.save(clienteActual);
    }

    @DeleteMapping("/cliente/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        clienteService.delete(id);
    }

}