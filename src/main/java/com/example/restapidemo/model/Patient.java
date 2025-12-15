package com.example.restapidemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    private Long id;
    private String nombre;
    private String descripcion;
    private int numeroCorto;
    private int numeroLargo;
    private String observaciones;
}
