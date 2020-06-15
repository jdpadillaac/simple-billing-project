package com.app.controllers;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;

import com.app.common.HandleValidError;
import com.app.dto.ClienteCreateDto;
import com.app.models.Cliente;
import com.app.models.JsonResp;
import com.app.models.Region;
import com.app.services.interfaces.ClienteService;
import com.app.services.interfaces.RegionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin()
@RestController
@RequestMapping("/api")
public class ClienteRestController {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private RegionService regionSrv;

    public JsonResp resp;
    private HandleValidError validErrors;

    public ClienteRestController() {
        resp = new JsonResp();
        validErrors = new HandleValidError();
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
    public ResponseEntity<?> create(@Valid @RequestBody ClienteCreateDto cliente, BindingResult result) {

        resp = new JsonResp();
        Cliente nuevoCliente;

        Region regionSeleccionhada = regionSrv.findById(cliente.getIdRegion());

        Cliente clienteAGuardar = new Cliente();
        clienteAGuardar.setApellidos(cliente.getApellido());
        clienteAGuardar.setNombre(cliente.getNombre());
        clienteAGuardar.setEmail(cliente.getEmail());
        clienteAGuardar.setRegion(regionSeleccionhada);

        System.out.println(clienteAGuardar);

        if (result.hasErrors()) {
            resp.success = false;
            resp.message = "Error en la validacion de datos";
            resp.error = validErrors.getErrorList(result);
            return new ResponseEntity<JsonResp>(resp, HttpStatus.BAD_REQUEST);
        }

        try {

            nuevoCliente = clienteService.save(clienteAGuardar);

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

    @PutMapping("/clientes/upload")
    public ResponseEntity<JsonResp> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id) {

        Cliente cliente;
        String nombreArchivo = null;

        try {
            cliente = clienteService.findById(id);
        } catch (DataAccessException e) {
            resp.success = false;
            resp.message = "Error en actualizacion de foto de cliente";
            resp.error = e.getMessage().concat(": ").concat(e.getMostSpecificCause().toString());
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!archivo.isEmpty()) {
            nombreArchivo = saveCustomerImage(archivo, cliente);
            cliente.setFoto(nombreArchivo);
            clienteService.save(cliente);
        }

        resp.message = "Cliente actualizadon de manera exitosa";
        resp.data = cliente;
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @GetMapping("/cliente/foto/{nombreFoto:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto) {

        Path rutaFotoAnterio = Paths.get("documents/clientes/foto").resolve(nombreFoto).toAbsolutePath();
        Resource recurso = null;

        try {
            recurso = new UrlResource(rutaFotoAnterio.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (!recurso.exists() && !recurso.isReadable()) {
            // deberia cargar una imagen por defecto aqui
        }

        HttpHeaders cabecera = new HttpHeaders();
        cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");
        return new ResponseEntity<Resource>(recurso, cabecera, HttpStatus.OK);
    }


    public String saveCustomerImage(MultipartFile archivo, Cliente cliente) {

        String nombreArchivo =  UUID.randomUUID().toString() + cliente.getNombre().concat("ID".concat(cliente.getId().toString()).concat("_").concat(archivo.getOriginalFilename().replace(" ", "")));
        Path rutaArchivo = Paths.get("documents/clientes/foto").resolve(nombreArchivo).toAbsolutePath();

        try {
            Files.copy(archivo.getInputStream(), rutaArchivo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }


        String nombreFotoAnterior = cliente.getFoto();

        if (nombreFotoAnterior != null && nombreFotoAnterior.length() > 5) {
            Path rutaFotoAnterio = Paths.get("documents/clientes/foto").resolve(nombreFotoAnterior).toAbsolutePath();

            File archivoAnterior = rutaFotoAnterio.toFile();

            if (archivoAnterior.exists() && archivoAnterior.canRead()) {
                archivoAnterior.delete();
            }
        }

        return nombreArchivo;
    }

}