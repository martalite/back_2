package com.example.restapidemo.controller;

import com.example.restapidemo.model.Login;
import com.example.restapidemo.model.Profile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Controlador REST para gestionar logins
 * Este controlador maneja todas las peticiones HTTP relacionadas con logins
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Login", description = "API para gestionar el inicio de sesión")
public class LoginController {

    // Lista de emails validos hardcodeada para tests
    private static final String[] VALID_USERS = {
        "juan@example.com", "jose@example.com", "pepito@example.com", "admin"
    };
    
    @PostMapping("/login")
    @Operation(summary = "Valida un login de un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login acceptado"),
            @ApiResponse(responseCode = "401", description = "Login no autorizado")
    })
    public ResponseEntity<Boolean> login(@RequestBody Login loginRequest) {
        
        String mail = loginRequest.getEmail();

        // mirar si existe en el array
        for (String validUser : VALID_USERS) {

            if (validUser.equalsIgnoreCase(mail)) {

                return ResponseEntity.status(HttpStatus.OK).body(true);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        // return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(false);
    }

}
