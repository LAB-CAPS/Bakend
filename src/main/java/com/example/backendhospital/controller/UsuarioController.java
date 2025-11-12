package com.example.backendhospital.controller;
import com.example.backendhospital.dto.LoginResponseDTO;
import com.example.backendhospital.dto.LoginDTO;
import com.example.backendhospital.dto.RegistroMedicoDTO;
import com.example.backendhospital.dto.RegistroPacienteDTO;
import com.example.backendhospital.entity.Medico;
import com.example.backendhospital.entity.Paciente;
import com.example.backendhospital.entity.Rol;
import com.example.backendhospital.entity.Usuario;
import com.example.backendhospital.repository.HistoriaClinicaRepository;
import com.example.backendhospital.repository.MedicoRepository;
import com.example.backendhospital.repository.PacienteRepository;
import com.example.backendhospital.service.UsuarioService;
import com.example.backendhospital.entity.Rol;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.backendhospital.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
private UsuarioRepository usuarioRepository;

@Autowired
private MedicoRepository medicoRepository;


    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;
   
@PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error al registrar usuario: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        return usuarioService.loginConDatos(loginDTO.getDni(), loginDTO.getContrasena())
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                               .body("Credenciales incorrectas o múltiples usuarios con mismo DNI."));
    }

    @GetMapping("/buscar/{dni}")
    public ResponseEntity<?> obtenerUsuarioPorDni(@PathVariable String dni) {
        return usuarioService.obtenerUsuarioPorDni(dni)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> listarUsuariosPorRol(@PathVariable String rol) {
        return ResponseEntity.ok(usuarioService.listarUsuariosPorRol(rol));
    }

    @PostMapping("/registrar-paciente")
    public ResponseEntity<?> registrarPaciente(@RequestBody RegistroPacienteDTO dto) {
        try {
            Paciente paciente = usuarioService.registrarPaciente(dto);
            return ResponseEntity.ok(paciente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error al registrar paciente: " + e.getMessage());
        }
    }

    @PostMapping("/registrar-medico")
    public ResponseEntity<?> registrarMedico(@RequestBody RegistroMedicoDTO dto) {
        try {
            Medico medico = usuarioService.registrarMedico(dto);
            return ResponseEntity.ok(medico);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error inesperado: " + e.getMessage());
        }
    }
@PostMapping("/login-admin")
public ResponseEntity<?> loginAdmin(@RequestBody LoginDTO loginDTO) {
    Optional<Usuario> usuarioOpt = usuarioRepository.findByDniAndContrasena(
        loginDTO.getDni(), loginDTO.getContrasena()
    );

    if (usuarioOpt.isPresent()) {
        Usuario usuario = usuarioOpt.get();

        // ✅ Comparación correcta con enum convertido desde String
        if (usuario.getRol().equalsIgnoreCase("ADMIN")) {
            LoginResponseDTO response = new LoginResponseDTO();
            response.setId((long) usuario.getId());
            response.setDni(usuario.getDni());
            response.setRol(usuario.getRol());
            response.setNombreUsuario(usuario.getNombreUsuario()); // ✅ Aquí corregido

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Acceso denegado: no es un administrador.");
        }
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Credenciales incorrectas.");
    }
}


}

