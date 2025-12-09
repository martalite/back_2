package com.example.restapidemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de datos para un Centro
 * Representa la información básica de un centro
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Center {

    private Long id;
    private String nombre;
    private String descripcion;
}
