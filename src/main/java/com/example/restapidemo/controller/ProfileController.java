package com.example.restapidemo.controller;

import com.example.restapidemo.model.Profile;
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
 * Controlador REST para gestionar perfiles
 * Este controlador maneja todas las peticiones HTTP relacionadas con perfiles
 */
@RestController
@RequestMapping("/api/profiles")
@Tag(name = "Profiles", description = "API para gestionar perfiles")
public class ProfileController {

    // Simulamos una base de datos en memoria con una lista
    private final List<Profile> profiles = new ArrayList<>();
    private Long nextId = 1L;

    // Constructor que inicializa algunos perfiles de ejemplo
    public ProfileController() {
        profiles.add(new Profile(nextId++, "Juan Pérez", "Exercitation id minim sint dolor ad. Est proident ipsum amet esse reprehenderit ipsum deserunt est cillum ad do magna. Ut laboris ea elit qui velit Lorem sit irure eiusmod ad est ipsum aliquip. Fugiat excepteur do veniam commodo ipsum dolor laboris dolor laboris deserunt. Pariatur ex deserunt Lorem dolor esse nisi magna ea ipsum.", "juan@example.com", 1));
        profiles.add(new Profile(nextId++, "María García", "Qui elit ullamco tempor ex aute incididunt. Minim consectetur do enim fugiat. Aliqua sint mollit nisi sint ex elit enim enim ipsum culpa. Reprehenderit eiusmod culpa ipsum quis ipsum quis occaecat. Ipsum enim veniam esse qui occaecat duis non aliquip. Eu qui cillum sit laboris tempor qui reprehenderit mollit non magna magna culpa velit qui. Sit officia qui amet qui cupidatat sit ad.", "maria@example.com", 2));
        profiles.add(new Profile(nextId++, "Carlos López", "Velit dolore laboris amet id. Minim aliquip est excepteur pariatur ut reprehenderit aliqua amet exercitation sunt adipisicing commodo eiusmod. Aliquip id cillum laborum sit anim qui id laborum aute amet ullamco. Qui reprehenderit velit irure ad aliqua excepteur voluptate ipsum sunt proident voluptate qui.", "carlos@example.com", 3));
    }

    /**
     * GET - Obtener todos los perfiles
     * Ejemplo: GET http://localhost:8080/api/profiles
     */
    @Operation(summary = "Obtener todos los perfiles", description = "Retorna una lista con todos los perfiles registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de perfiles obtenida exitosamente")
    public ResponseEntity<List<Profile>> getAllProfiles() {
        return ResponseEntity.ok(profiles);
    }

    /**
     * GET - Obtener todos los perfiles b
     * Ejemplo: GET http://localhost:8080/api/profiles
     */
    @GetMapping("/all")
    @Operation(summary = "Obtener todos los perfiles b", description = "Retorna una lista con todos los perfiles registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de perfiles obtenida exitosamente")
    public ResponseEntity<List<Profile>> getAllProfilesB() {
        return ResponseEntity.ok(profiles);
    }

    /**
     * GET - Obtener un perfil por ID
     * Ejemplo: GET http://localhost:8080/api/profiles/1
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un perfil por ID", description = "Retorna la información de un perfil específico basándose en su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    public ResponseEntity<Profile> getProfileById(
            @Parameter(description = "ID del perfil a buscar", required = true) @PathVariable Long id) {

        Optional<Profile> Profile = profiles.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        return Profile.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET - Buscar perfiles por nombre
     * Ejemplo: GET http://localhost:8080/api/profiles/search?nombre=Juan
     */
    @GetMapping("/search")
    @Operation(summary = "Buscar perfiles por nombre", description = "Busca perfiles cuyo nombre contenga el texto especificado (case-insensitive)")
    @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente")
    public ResponseEntity<List<Profile>> searchProfilesByName(
            @Parameter(description = "Texto a buscar en el nombre del perfil") @RequestParam(required = false) String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return ResponseEntity.ok(profiles);
        }

        List<Profile> filteredProfiles = profiles.stream()
                .filter(u -> u.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .toList();

        return ResponseEntity.ok(filteredProfiles);
    }

    /**
     * POST - Crear un nuevo perfil
     * Ejemplo: POST http://localhost:8080/api/profiles
     * Body: { "nombre": "Ana Torres", "email": "ana@example.com", "edad": 28 }
     */
    @PostMapping
    @Operation(summary = "Crear un nuevo perfil", description = "Crea un nuevo perfil en el sistema. El ID se asigna automáticamente.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Perfil creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de perfil inválidos")
    })
    public ResponseEntity<Profile> createProfile(@RequestBody Profile Profile) {
        Profile.setId(nextId++);
        profiles.add(Profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(Profile);
    }

    /**
     * PUT - Actualizar un perfil existente
     * Ejemplo: PUT http://localhost:8080/api/profiles/1
     * Body: { "nombre": "Juan Pérez Actualizado", "email":
     * "juan.nuevo@example.com", "edad": 31 }
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un perfil", description = "Actualiza la información completa de un perfil existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    public ResponseEntity<Profile> updateProfile(
            @Parameter(description = "ID del perfil a actualizar", required = true) @PathVariable Long id,
            @RequestBody Profile updatedProfile) {

        Optional<Profile> existingProfile = profiles.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        if (existingProfile.isPresent()) {
            Profile Profile = existingProfile.get();
            Profile.setNombre(updatedProfile.getNombre());
            Profile.setDescripcion(updatedProfile.getNombre());
            if (updatedProfile.getEmailDeContacto() != null) {
                Profile.setEmailDeContacto(updatedProfile.getEmailDeContacto());
            }
            Profile.setIdUsuario(updatedProfile.getIdUsuario());
            return ResponseEntity.ok(Profile);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * DELETE - Eliminar un perfil
     * Ejemplo: DELETE http://localhost:8080/api/profiles/1
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un perfil", description = "Elimina un perfil del sistema basándose en su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Perfil eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Perfil no encontrado")
    })
    public ResponseEntity<Void> deleteProfile(
            @Parameter(description = "ID del perfil a eliminar", required = true) @PathVariable Long id) {

        boolean removed = profiles.removeIf(u -> u.getId().equals(id));

        if (removed) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
