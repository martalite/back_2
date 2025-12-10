package com.example.restapidemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de datos para la relación entre un usuario y un centro
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCenter {

    private Long idUsuario;
    private Long idCentro;
}
