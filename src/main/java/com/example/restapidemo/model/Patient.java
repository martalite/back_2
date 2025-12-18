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
    private String apellido1;
    private String apellido2;
    private String fechaDeNacimiento;
    private String sexo;
    private String dni;

    private int numeroTelefono;
    private String email;

    private String colorFichaMedica;

    private String[] diagnosticos;

    private String[] comentariosDePaciente;
}
