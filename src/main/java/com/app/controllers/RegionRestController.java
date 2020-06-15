package com.app.controllers;

import java.util.List;

import javax.validation.Valid;

import com.app.common.HandleValidError;
import com.app.dto.RegionCreateDto;
import com.app.models.JsonResp;
import com.app.models.Region;
import com.app.services.interfaces.RegionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class RegionRestController {

    private JsonResp resp = new JsonResp();
    private HandleValidError validErrors = new HandleValidError();

    @Autowired
    RegionService regionSrv;

    @PostMapping("/region")
    public ResponseEntity<JsonResp> save(@Valid @RequestBody RegionCreateDto region, BindingResult result) {

        resp = new JsonResp();
        Region regionSaved = null;

        if (result.hasErrors()) {
            resp.success = false;
            resp.message = "Error en la validacion de datos";
            resp.error = validErrors.getErrorList(result);
            return new ResponseEntity<JsonResp>(resp, HttpStatus.BAD_REQUEST);
        }

        try {
            Region newRegion = new Region();
            newRegion.setNombre(region.getName());
            newRegion.setCodigo(region.getCode());
            regionSaved = regionSrv.save(newRegion);
        } catch (DataAccessException e) {
            resp.success = false;
            resp.message = "Error en servidor al registrar un region";
            resp.error = e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage());
            return new ResponseEntity<JsonResp>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        resp.success = true;
        resp.message = "Region registrada de manera exitosa";
        resp.error = regionSaved;

        return new ResponseEntity<JsonResp>(resp, HttpStatus.CREATED);
    }

    @GetMapping("/regiones")
    public ResponseEntity<JsonResp> getAll() {

        resp = new JsonResp();
        List<Region> regiones = null;

        try {
            regiones = regionSrv.findAll();
        } catch (DataAccessException e) {
            resp.success = false;
            resp.message = "Error en servidor al cargar listado de regiones";
            resp.error = e.getMessage().concat(": ").concat(e.getMostSpecificCause().toString());
            return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        resp.message = "Listado de regiones cargado correctamente";
        resp.data = regiones;
        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}