package com.example.backendhospital.controller;

import com.example.backendhospital.dto.MedicoDTO;
import com.example.backendhospital.entity.Medico;
import com.example.backendhospital.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoRepository medicoRepository;

  @GetMapping
public ResponseEntity<List<MedicoDTO>> obtenerTodosLosMedicos() {
    List<Medico> medicos = medicoRepository.findAll();

    List<MedicoDTO> dtos = medicos.stream().map(m -> {
        MedicoDTO dto = new MedicoDTO();
        dto.setId(m.getId());
        dto.setDni(m.getDni());
        dto.setCorreo(m.getCorreo());
        dto.setTelefono(m.getTelefono());
        dto.setNombres(m.getNombres());
        dto.setApellidos(m.getApellidos());
        dto.setCodigoMedico(m.getCodigoMedico());

        // ✅ Validación por si algún médico no tiene especialidad
        dto.setEspecialidad(
            m.getEspecialidad() != null ? m.getEspecialidad().getNombre() : "Sin especialidad"
        );

        return dto;
    }).collect(Collectors.toList());

    return ResponseEntity.ok(dtos);
}

    @GetMapping("/obtener-id/{idUsuario}")
public ResponseEntity<?> obtenerIdMedico(@PathVariable Long idUsuario) {
    var medico = medicoRepository.findByUsuarioId(idUsuario)
            .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID de usuario: " + idUsuario));
    return ResponseEntity.ok(Map.of("idMedico", medico.getId()));
}

@GetMapping("/usuario/{idUsuario}/id-medico")
public ResponseEntity<Map<String, Long>> obtenerIdMedicoPorUsuario(@PathVariable Long idUsuario) {
    Medico medico = medicoRepository.findByUsuarioId(idUsuario)
        .orElseThrow(() -> new RuntimeException("No se encontró un médico con ese usuario"));

    Map<String, Long> respuesta = new HashMap<>();
    respuesta.put("idMedico", medico.getId());
    return ResponseEntity.ok(respuesta);
}


}
