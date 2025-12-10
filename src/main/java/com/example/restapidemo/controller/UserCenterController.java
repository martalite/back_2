package com.example.restapidemo.controller;

import com.example.restapidemo.model.Center;
import com.example.restapidemo.model.UserCenter;

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
 * Controlador REST para gestionar la relación de usuarios y centros
 * Este controlador maneja todas las peticiones HTTP relacionadas con centros
 */
@RestController
@RequestMapping("/api/usersCenters")
@Tag(name = "Users Centers", description = "API para gestionar la relación de usuarios y centros")
public class UserCenterController {

    // Simulamos una base de datos en memoria con una lista
    private final List<UserCenter> usersCenters = new ArrayList<>();

    // Constructor que inicializa algunos perfiles de ejemplo
    public UserCenterController() {
        usersCenters.add(new UserCenter(1L, 1L));
        usersCenters.add(new UserCenter(1L, 3L));
        usersCenters.add(new UserCenter(2L, 2L));
    }

    /**
     * GET - Obtener todos los perfiles
     * Ejemplo: GET http://localhost:8080/api/usersCenters
     */
    @Operation(summary = "Obtener todos los perfiles", description = "Retorna una lista con todos los perfiles registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de perfiles obtenida exitosamente")
    public ResponseEntity<List<UserCenter>> getAllCenters() {
        return ResponseEntity.ok(usersCenters);
    }

    /**
     * GET - Obtener todos los perfiles b
     * Ejemplo: GET http://localhost:8080/api/usersCenters
     */
    @GetMapping("/all")
    @Operation(summary = "Obtener todos los perfiles b", description = "Retorna una lista con todos los perfiles registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de perfiles obtenida exitosamente")
    public ResponseEntity<List<UserCenter>> getAllCentersB() {
        return ResponseEntity.ok(usersCenters);
    }

    /**
     * GET - Obtener un UsuarioCentro por id usuario y id centro
     * Ejemplo: GET http://localhost:8080/api/usersCenters/1
     */
    @GetMapping("/{idUsuario}/{idCentro}")
    @Operation(summary = "Obtener un UsuarioCentro por ID", description = "Por id usuario y id centro")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Centro encontrado"),
            @ApiResponse(responseCode = "404", description = "Centro no encontrado")
    })
    public ResponseEntity<UserCenter> getCenterById(
            @Parameter(description = "Id del usuario", required = true) @PathVariable Long idUsuario,
            @Parameter(description = "Id del centro", required = true) @PathVariable Long idCentro) {

        Optional<UserCenter> userCenter = usersCenters.stream()
                .filter(u -> (u.getIdUsuario() == idUsuario) && (u.getIdCentro() == idCentro))
                .findFirst();

        return userCenter.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET - Buscar centros assignados a un usuario
     * Ejemplo: GET http://localhost:8080/api/usersCenters/search?nombre=Juan
     */
    @GetMapping("/search")
    @Operation(summary = "Buscar centros assignados a un usuario", description = "Busca todos los centros de un usuario por id de usuario")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    public ResponseEntity<List<UserCenter>> searchUserCentersByUserId(
            @Parameter(description = "Id del usuario a buscar") @RequestParam(required = true) Long idUsuario) {

        if (idUsuario == null) {
            return ResponseEntity.ok(usersCenters);
        }

        List<UserCenter> filteredUsersCenters = usersCenters.stream()
                .filter(u -> u.getIdUsuario() == idUsuario)
                .toList();

        return ResponseEntity.ok(filteredUsersCenters);
    }

    /**
     * POST - Crear un nuevo centro
     * Ejemplo: POST http://localhost:8080/api/usersCenters
     * Body: { "nombre": "Ana Torres", "email": "ana@example.com", "edad": 28 }
     */
    @PostMapping
    @Operation(summary = "Crear un nuevo centro", description = "Crea un nuevo centro en el sistema. El ID se asigna automáticamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Centro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de centro inválidos")
    })
    public ResponseEntity<UserCenter> createCenter(@RequestBody UserCenter userCenter) {
        usersCenters.add(userCenter);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCenter);
    }

    /**
     * PUT - Actualizar un centro existente
     * Ejemplo: PUT http://localhost:8080/api/usersCenters/1
     * Body: { "nombre": "Juan Pérez Actualizado", "email":
     * "juan.nuevo@example.com", "edad": 31 }
     */
    // @PutMapping("/{id}")
    // @Operation(summary = "Actualizar un centro", description = "Actualiza la
    // información completa de un centro existente")
    // @ApiResponses({
    // @ApiResponse(responseCode = "200", description = "Centro actualizado
    // exitosamente"),
    // @ApiResponse(responseCode = "404", description = "Centro no encontrado")
    // })
    // public ResponseEntity<UserCenter> updateCenter(
    // @Parameter(description = "ID del centro a actualizar", required = true)
    // @PathVariable Long id,
    // @RequestBody UserCenter updatedCenter) {

    // Optional<UserCenter> existingCenter = usersCenters.stream()
    // .filter(u -> u.getIdUsuario().equals(id))
    // .findFirst();

    // if (existingCenter.isPresent()) {
    // UserCenter userCenter = existingCenter.get();
    // return ResponseEntity.ok(userCenter);
    // }

    // return ResponseEntity.notFound().build();
    // }

    /**
     * DELETE - Eliminar un centro
     * Ejemplo: DELETE http://localhost:8080/api/usersCenters/1
     */
    @DeleteMapping("/{idUsuario}/{idCentro}")
    @Operation(summary = "Eliminar un centro", description = "Elimina un centro del sistema basándose en su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Centro eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Centro no encontrado")
    })
    public ResponseEntity<Void> deleteCenter(
            @Parameter(description = "ID del centro a eliminar", required = true) @PathVariable Long idUsuario,
            @Parameter(description = "ID del centro a eliminar", required = true) @PathVariable Long idCentro) {

        boolean removed = usersCenters.removeIf(u -> (u.getIdUsuario() == idUsuario) && (u.getIdCentro() == idCentro));

        if (removed) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
