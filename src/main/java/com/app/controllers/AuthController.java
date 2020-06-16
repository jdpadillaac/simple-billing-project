package com.app.controllers;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import com.app.auth.JwtProvider;
import com.app.common.HandleValidError;
import com.app.dto.JwtDto;
import com.app.dto.LoginDto;
import com.app.dto.UsuarioCrearDto;
import com.app.enums.RolNombre;
import com.app.models.JsonResp;
import com.app.models.Rol;
import com.app.models.Usuario;
import com.app.services.interfaces.RolService;
import com.app.services.interfaces.UsuarioService;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.JwtParser;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private JsonResp resp;
    private HandleValidError validErrors;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired 
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    public AuthController() {
        resp = new JsonResp();
        validErrors = new HandleValidError();
    }

    @PostMapping("/nuevo")
    public ResponseEntity<JsonResp> nuevo(@Valid @RequestBody UsuarioCrearDto usuario, BindingResult result ) {

        if (result.hasErrors()) {
            resp.success = false;
            resp.message = "Error en la validacion de datos";
            resp.error = validErrors.getErrorList(result);
            return new ResponseEntity<JsonResp>(resp, HttpStatus.BAD_REQUEST);
        }

        Usuario newUsuario = new Usuario();
        newUsuario.setEmail(usuario.getEmail());
        newUsuario.setNombre(usuario.getNombre());
        newUsuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        newUsuario.setUsername(usuario.getUserName());
        
        Set<Rol> roles = new HashSet<>();

        roles.add(rolService.getByRolNombre(RolNombre.ROL_USER).get());

        if (usuario.getRoles().contains("admin")) {
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        }

        newUsuario.setRoles(roles);

        System.out.println(newUsuario);
        Usuario usuarioCreado = usuarioService.save(newUsuario);

        resp.message = "Usuario registrado de manera exitosa";
        resp.data = usuarioCreado;
        return new ResponseEntity<JsonResp>(resp, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<JsonResp> login(@Valid @RequestBody LoginDto loginUser, BindingResult results) {

        if (results.hasErrors()) {
            resp.success = false;
            resp.message = "Error en la validacion de datos";
            resp.error = validErrors.getErrorList(results);
            return new ResponseEntity<JsonResp>(resp, HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = 
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());

        resp.message = "Login exitoso";
        resp.data = jwtDto;
        return new ResponseEntity<JsonResp>(resp, HttpStatus.OK);


    }
}