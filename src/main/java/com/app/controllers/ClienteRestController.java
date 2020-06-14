package com.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import javax.validation.Valid;
import com.app.models.Cliente;
import com.app.models.JsonResp;
import com.app.services.interfaces.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin()
@RestController
@RequestMapping("/api")
public class ClienteRestController {

    @Autowired
    private ClienteService clienteService;

    public JsonResp resp;

    public ClienteRestController() {
        resp = new JsonResp();
    }

    @GetMapping("/clientes")
    public ResponseEntity<JsonResp> index() {

        List<Cliente> listaClientes = null;

        try {

            listaClientes = clienteService.findAll();

        } catch (DataAccessException e) {
            resp.success = false;
            resp.message = "Listado de cleintes cargaod correctamente";
            resp.error = e.getMessage().concat(": ").concat(e.getMostSpecificCause().toString());
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        resp.message = "Listado de cleintes cargaod correctamente";
        resp.data = listaClientes;
        return new ResponseEntity<>(resp, HttpStatus.ACCEPTED);
    }

    @GetMapping("cliente/{id}")
    public ResponseEntity<JsonResp> show(@PathVariable Long id) {

        Cliente cliente = null;

        try {
            cliente = clienteService.findById(id);

            Map<String, Object> response = new HashMap<>();
            if (cliente == null) {
                response.put("errorDetail",
                        "No existe cliente con el ID: ".concat(id.toString().concat(" No existe en la base de datos")));

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
    public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {

        Cliente nuevoCliente;

        if (result.hasErrors()) {

            List<String> errors = result.getFieldErrors().stream()
                    .map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
                    .collect(Collectors.toList());

            resp.success = false;
            resp.message = "Error de validación - Datos enviados incorrectamente";
            resp.error = errors;
            return new ResponseEntity<JsonResp>(resp, HttpStatus.BAD_REQUEST);
        }

        try {

            nuevoCliente = clienteService.save(cliente);

        } catch (DataAccessException e) {
            resp.success = false;
            resp.message = "Error en insertoar registro en la base de datos";
            resp.error = e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage());
            return new ResponseEntity<JsonResp>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        resp.message = "CLiente registrado correctamente";
        resp.data = nuevoCliente;
        return new ResponseEntity<JsonResp>(resp, HttpStatus.CREATED);
    }

    @PutMapping("/cliente/{id}")
    public ResponseEntity<JsonResp> update(@RequestBody Cliente cliente, @PathVariable long id) {

        Cliente clienteActual = null;
        Cliente clienteGuardado = null;

        try {

            clienteActual = clienteService.findById(id);

            if (clienteActual == null) {
                resp.success = false;
                resp.message = "Cliente no encontrado";
                resp.error = "No existe cliente con id " + id + " en la base de datos";
                return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
            }

            clienteActual.setApellidos(cliente.getApellidos());
            clienteActual.setNombre(cliente.getNombre());
            clienteActual.setEmail(cliente.getEmail());
            clienteGuardado = clienteService.save(clienteActual);

        } catch (DataAccessException e) {
            resp.success = false;
            resp.message = "Error en consulta e insercion en la base de datos";
            resp.error = e.getMessage().concat(": ").concat(e.getMostSpecificCause().toString());
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        resp.message = "Cliente actualizadon de manera exitosa";
        resp.data = clienteGuardado;
        return new ResponseEntity<>(resp, HttpStatus.CREATED);

    }

    @DeleteMapping("/cliente/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        clienteService.delete(id);
    }

}