package com.example.restapidemo.controller;

import com.example.restapidemo.model.Patient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patients", description = "API para gestionar pacientes")
public class PatientController {

    private final List<Patient> patients = new ArrayList<>();
    private Long nextId = 1L;

    public PatientController() {

        patients.add(new Patient(nextId++, "Paciente 1", "Est reprehenderit incididunt ullamco mollit.", 32, 2342354, "Es normal, parece.", ""));
        patients.add(new Patient(nextId++, "Paciente 2", "Qui elit ullamco tempor ex aute incididunt.", 34, 14123, "Necessita toda una fábrica para cubrir su consumo.", ""));
        patients.add(new Patient(nextId++, "Paciente 3", "Velit dolore laboris amet id.", 12, 2313, "Se ha roto el pie como 3 veces.", ""));
        patients.add(new Patient(nextId++, "Paciente 4", "Velit dolore laboris amet id.", 13, 123123, "Sin observaciones.", ""));
        patients.add(new Patient(nextId++, "Paciente 5", "Velit dolore laboris amet id.", 54, 7654, "No tiene mucho dinero.", ""));
        patients.add(new Patient(nextId++, "Paciente 6", "Velit dolore laboris amet id.", 98, 6764543, "Tiene dinero.", ""));
    }

    
    @Operation(summary = "Obtener todos los pacientes", description = "Retorna una lista con todos los pacientes registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida exitosamente")
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patients);
    }

   
    @GetMapping("/all")
    @Operation(summary = "Obtener todos los pacientes", description = "Retorna una lista con todos los pacientes registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes obtenida exitosamente")
    public ResponseEntity<List<Patient>> getAllPatientsB() {
        return ResponseEntity.ok(patients);
    }

   
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un paciente por ID", description = "Retorna la información de un paciente específico basándose en su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    public ResponseEntity<Patient> getPatientById(
            @Parameter(description = "ID del paciente a buscar", required = true) @PathVariable Long id) {

        Optional<Patient> Patient = patients.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        return Patient.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

 
    @GetMapping("/search")
    @Operation(summary = "Buscar pacientes por nombre", description = "Busca pacientes cuyo nombre contenga el texto especificado (case-insensitive)")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    public ResponseEntity<List<Patient>> searchPatientsByName(
            @Parameter(description = "Texto a buscar en el nombre del paciente") @RequestParam(required = false) String nombre,
            @Parameter(description = "Texto a buscar en la descripción del paciente") @RequestParam(required = false) String descripcion,
            @Parameter(description = "Código corto a buscar") @RequestParam(required = false) Integer numeroCorto) {

        List<Patient> filteredPatients = patients;

        // Solo aplicar filtros si existen
        if (nombre != null && !nombre.trim().isEmpty()) {

            filteredPatients = filteredPatients.stream()
                .filter(u -> u.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();
        }

        if (descripcion != null) {
            filteredPatients = filteredPatients.stream()
                .filter(u -> u.getDescripcion().toLowerCase().contains(descripcion.toLowerCase()))
                .toList();
        }

        if (numeroCorto != null) {
            filteredPatients = filteredPatients.stream()
                .filter(u -> u.getNumeroCorto() == numeroCorto)
                .toList();
        }
       
        return ResponseEntity.ok(filteredPatients);
    }


    @PostMapping
    @Operation(summary = "Crear un nuevo paciente", description = "Crea un nuevo paciente en el sistema. El ID se asigna automáticamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Paciente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de paciente inválidos")
    })
    public ResponseEntity<Patient> createPatient(@RequestBody Patient Patient) {
        Patient.setId(nextId++);
        patients.add(Patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(Patient);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un paciente", description = "Actualiza la información completa de un paciente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    public ResponseEntity<Patient> updatePatient(
            @Parameter(description = "ID del paciente a actualizar", required = true) @PathVariable Long id,
            @RequestBody Patient updatedPatient) {

        Optional<Patient> existingPatient = patients.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        if (existingPatient.isPresent()) {
            Patient Patient = existingPatient.get();
            Patient.setNombre(updatedPatient.getNombre());
            Patient.setDescripcion(updatedPatient.getDescripcion());
            return ResponseEntity.ok(Patient);
        }

        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un paciente", description = "Elimina un paciente del sistema basándose en su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Paciente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado")
    })
    public ResponseEntity<Void> deletePatient(
            @Parameter(description = "ID del paciente a eliminar", required = true) @PathVariable Long id) {

        boolean removed = patients.removeIf(u -> u.getId().equals(id));

        if (removed) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
