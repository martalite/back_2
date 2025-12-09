package com.example.restapidemo.controller;

import com.example.restapidemo.model.Center;
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

/**
 * Controlador REST para gestionar Centros
 * Este controlador maneja todas las peticiones HTTP relacionadas con centros
 */
@RestController
@RequestMapping("/api/centers")
@Tag(name = "Centers", description = "API para gestionar centros")
public class CenterController {

    // Simulamos una base de datos en memoria con una lista
    private final List<Center> centers = new ArrayList<>();
    private Long nextId = 1L;

    // Constructor que inicializa algunos perfiles de ejemplo
    public CenterController() {
        centers.add(new Center(nextId++, "Centro 1", "Est reprehenderit incididunt ullamco mollit. Culpa ad qui id cillum excepteur. Dolor incididunt aliquip reprehenderit do culpa ut sunt ea. Excepteur esse ea occaecat tempor commodo voluptate cillum nulla. Est ad esse id qui in consequat sit aliqua sunt incididunt."));
        centers.add(new Center(nextId++, "Centro 2", "Qui elit ullamco tempor ex aute incididunt. Minim consectetur do enim fugiat. Aliqua sint mollit nisi sint ex elit enim enim ipsum culpa. Reprehenderit eiusmod culpa ipsum quis ipsum quis occaecat. Ipsum enim veniam esse qui occaecat duis non aliquip. Eu qui cillum sit laboris tempor qui reprehenderit mollit non magna magna culpa velit qui. Sit officia qui amet qui cupidatat sit ad."));
        centers.add(new Center(nextId++, "Centro 3", "Velit dolore laboris amet id. Minim aliquip est excepteur pariatur ut reprehenderit aliqua amet exercitation sunt adipisicing commodo eiusmod. Aliquip id cillum laborum sit anim qui id laborum aute amet ullamco. Qui reprehenderit velit irure ad aliqua excepteur voluptate ipsum sunt proident voluptate qui."));
    }

    /**
     * GET - Obtener todos los perfiles
     * Ejemplo: GET http://localhost:8080/api/centers
     */
    @Operation(summary = "Obtener todos los perfiles", description = "Retorna una lista con todos los perfiles registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de perfiles obtenida exitosamente")
    public ResponseEntity<List<Center>> getAllCenters() {
        return ResponseEntity.ok(centers);
    }

    /**
     * GET - Obtener todos los perfiles b
     * Ejemplo: GET http://localhost:8080/api/centers
     */
    @GetMapping("/all")
    @Operation(summary = "Obtener todos los perfiles b", description = "Retorna una lista con todos los perfiles registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de perfiles obtenida exitosamente")
    public ResponseEntity<List<Center>> getAllCentersB() {
        return ResponseEntity.ok(centers);
    }

    /**
     * GET - Obtener un centro por ID
     * Ejemplo: GET http://localhost:8080/api/centers/1
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un centro por ID", description = "Retorna la información de un centro específico basándose en su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Centro encontrado"),
            @ApiResponse(responseCode = "404", description = "Centro no encontrado")
    })
    public ResponseEntity<Center> getCenterById(
            @Parameter(description = "ID del centro a buscar", required = true) @PathVariable Long id) {

        Optional<Center> Center = centers.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        return Center.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET - Buscar perfiles por nombre
     * Ejemplo: GET http://localhost:8080/api/centers/search?nombre=Juan
     */
    @GetMapping("/search")
    @Operation(summary = "Buscar perfiles por nombre", description = "Busca perfiles cuyo nombre contenga el texto especificado (case-insensitive)")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    public ResponseEntity<List<Center>> searchCentersByName(
            @Parameter(description = "Texto a buscar en el nombre del centro") @RequestParam(required = false) String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return ResponseEntity.ok(centers);
        }

        List<Center> filteredCenters = centers.stream()
                .filter(u -> u.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();

        return ResponseEntity.ok(filteredCenters);
    }

    /**
     * POST - Crear un nuevo centro
     * Ejemplo: POST http://localhost:8080/api/centers
     * Body: { "nombre": "Ana Torres", "email": "ana@example.com", "edad": 28 }
     */
    @PostMapping
    @Operation(summary = "Crear un nuevo centro", description = "Crea un nuevo centro en el sistema. El ID se asigna automáticamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Centro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de centro inválidos")
    })
    public ResponseEntity<Center> createCenter(@RequestBody Center Center) {
        Center.setId(nextId++);
        centers.add(Center);
        return ResponseEntity.status(HttpStatus.CREATED).body(Center);
    }

    /**
     * PUT - Actualizar un centro existente
     * Ejemplo: PUT http://localhost:8080/api/centers/1
     * Body: { "nombre": "Juan Pérez Actualizado", "email":
     * "juan.nuevo@example.com", "edad": 31 }
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un centro", description = "Actualiza la información completa de un centro existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Centro actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Centro no encontrado")
    })
    public ResponseEntity<Center> updateCenter(
            @Parameter(description = "ID del centro a actualizar", required = true) @PathVariable Long id,
            @RequestBody Center updatedCenter) {

        Optional<Center> existingCenter = centers.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        if (existingCenter.isPresent()) {
            Center Center = existingCenter.get();
            Center.setNombre(updatedCenter.getNombre());
            Center.setDescripcion(updatedCenter.getDescripcion());
            return ResponseEntity.ok(Center);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE - Eliminar un centro
     * Ejemplo: DELETE http://localhost:8080/api/centers/1
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un centro", description = "Elimina un centro del sistema basándose en su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Centro eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Centro no encontrado")
    })
    public ResponseEntity<Void> deleteCenter(
            @Parameter(description = "ID del centro a eliminar", required = true) @PathVariable Long id) {

        // TODO: hacer que se mire si el centro se utiliza por algun usuario

        boolean removed = centers.removeIf(u -> u.getId().equals(id));

        if (removed) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
