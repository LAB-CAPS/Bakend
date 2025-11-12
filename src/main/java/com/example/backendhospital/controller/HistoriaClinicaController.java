package com.example.backendhospital.controller;

import com.example.backendhospital.dto.HistoriaClinicaDTO;
import com.example.backendhospital.dto.HistoriaClinicaResponseDTO;
import com.example.backendhospital.entity.HistoriaClinica;
import com.example.backendhospital.repository.HistoriaClinicaRepository;
import com.example.backendhospital.repository.MedicoRepository;
import com.example.backendhospital.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/historia-clinica")
@RequiredArgsConstructor
public class HistoriaClinicaController {

    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarHistoria(@RequestBody HistoriaClinicaDTO dto) {
        try {
            var paciente = pacienteRepository.findByDni(dto.getDniPaciente())
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

            var medico = medicoRepository.findById(dto.getIdMedico())
                    .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

            HistoriaClinica historia = new HistoriaClinica();
            historia.setPaciente(paciente);
            historia.setMedico(medico);
            historia.setFecha(LocalDate.now());
            historia.setDiagnostico(dto.getDiagnostico());
            historia.setNotas(dto.getNotas());
            historia.setMedicamentos(dto.getMedicamentos());

            historiaClinicaRepository.save(historia);

            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Historia clínica registrada correctamente");
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/por-dni/{dni}")
    public ResponseEntity<?> obtenerHistoriasPorDni(@PathVariable String dni) {
        var paciente = pacienteRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        var historias = historiaClinicaRepository.findByPaciente_Id(paciente.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        var response = historias.stream().map(historia -> {
            var dto = new HistoriaClinicaResponseDTO();
            dto.setId((long) historia.getId());
            dto.setFecha(historia.getFecha().format(formatter));
            dto.setDiagnostico(historia.getDiagnostico());
            dto.setNotas(historia.getNotas());
            dto.setMedicamentos(historia.getMedicamentos());

            if (historia.getMedico() != null) {
                dto.setNombreMedico(historia.getMedico().getNombres() + " " + historia.getMedico().getApellidos());
            } else {
                dto.setNombreMedico("N/D");
            }

            if (historia.getPaciente() != null) {
                dto.setNombrePaciente(historia.getPaciente().getNombres() + " " + historia.getPaciente().getApellidos());
            } else {
                dto.setNombrePaciente("N/D");
            }

            return dto;
        }).toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarHistoria(@PathVariable int id, @RequestBody HistoriaClinicaDTO dto) {
        var historia = historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia no encontrada"));

        historia.setDiagnostico(dto.getDiagnostico());
        historia.setNotas(dto.getNotas());
        historia.setMedicamentos(dto.getMedicamentos());

        historiaClinicaRepository.save(historia);
        return ResponseEntity.ok(Map.of("mensaje", "Historia actualizada"));
    }

    @GetMapping("/paciente/{idPaciente}")
    public ResponseEntity<?> obtenerHistoriasPorPaciente(@PathVariable Long idPaciente) {
        var historias = historiaClinicaRepository.findByPaciente_Id(idPaciente);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        var response = historias.stream().map(historia -> {
            var dto = new HistoriaClinicaResponseDTO();
            dto.setId((long) historia.getId());
            dto.setFecha(historia.getFecha().format(formatter));
            dto.setDiagnostico(historia.getDiagnostico());
            dto.setNotas(historia.getNotas());
            dto.setMedicamentos(historia.getMedicamentos());

            if (historia.getMedico() != null) {
                dto.setNombreMedico(historia.getMedico().getNombres() + " " + historia.getMedico().getApellidos());
            } else {
                dto.setNombreMedico("N/D");
            }

            if (historia.getPaciente() != null) {
                dto.setNombrePaciente(historia.getPaciente().getNombres() + " " + historia.getPaciente().getApellidos());
            } else {
                dto.setNombrePaciente("N/D");
            }

            return dto;
        }).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/todos")
    public ResponseEntity<?> obtenerTodoElHistorial() {
        var historias = historiaClinicaRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        var response = historias.stream().map(historia -> {
            var dto = new HistoriaClinicaResponseDTO();
            dto.setId((long) historia.getId());
            dto.setFecha(historia.getFecha().format(formatter));
            dto.setDiagnostico(historia.getDiagnostico());
            dto.setNotas(historia.getNotas());
            dto.setMedicamentos(historia.getMedicamentos());

            if (historia.getMedico() != null) {
                dto.setNombreMedico(historia.getMedico().getNombres() + " " + historia.getMedico().getApellidos());
            } else {
                dto.setNombreMedico("N/D");
            }

            if (historia.getPaciente() != null) {
                dto.setNombrePaciente(historia.getPaciente().getNombres() + " " + historia.getPaciente().getApellidos());
            } else {
                dto.setNombrePaciente("N/D");
            }

            return dto;
        }).toList();

        return ResponseEntity.ok(response);
    }
}
