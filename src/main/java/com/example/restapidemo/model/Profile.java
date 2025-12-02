package com.example.restapidemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de datos para un Perfil
 * Representa la información básica de un perfil para un Usuario
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    private Long id;
    private String nombre;
    private String descripcion;
    private String emailDeContacto;
    private int idUsuario;
}
