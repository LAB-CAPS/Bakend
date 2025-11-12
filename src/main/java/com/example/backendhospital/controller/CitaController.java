package com.example.backendhospital.controller;

import com.example.backendhospital.dto.CitaDTO;
import com.example.backendhospital.dto.CitaResponseDTO;
import com.example.backendhospital.dto.RecetaInputDTO;
import com.example.backendhospital.entity.Cita;
import com.example.backendhospital.entity.HistoriaClinica;
import com.example.backendhospital.entity.Medicamento;
import com.example.backendhospital.entity.Medico;
import com.example.backendhospital.entity.Paciente;
import com.example.backendhospital.entity.Receta;
import com.example.backendhospital.repository.CitaRepository;
import com.example.backendhospital.repository.HistoriaClinicaRepository;
import com.example.backendhospital.repository.MedicoRepository;
import com.example.backendhospital.repository.MedicamentoRepository;
import com.example.backendhospital.repository.PacienteRepository;
import com.example.backendhospital.repository.RecetaRepository;
import com.example.backendhospital.service.CitaService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaRepository citaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    private final CitaService citaService;

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;


 @PostMapping("/reservar")
public ResponseEntity<?> reservarCita(@RequestBody CitaDTO dto) {
    try {
        Paciente paciente = pacienteRepository.findById(dto.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Medico medico = medicoRepository.findById(dto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        LocalDate fecha = LocalDate.parse(dto.getFecha());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm[:ss]");
        LocalTime hora = LocalTime.parse(dto.getHora(), formatter).withSecond(0).withNano(0);

        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now().withSecond(0).withNano(0);

        // ❌ Validar fecha pasada
        if (fecha.isBefore(hoy) || (fecha.isEqual(hoy) && hora.isBefore(ahora))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "❌ No se pueden reservar citas en fechas u horas pasadas."));
        }

        // ❌ Validar límite diario
        List<Cita> citasDelDia = citaRepository.findByMedicoAndFecha(medico, fecha);
        if (citasDelDia.size() >= 10) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "❌ El médico ya tiene 10 citas para ese día."));
        }

        // ❌ NUEVA VALIDACIÓN: evitar duplicado en misma hora
        boolean yaReservado = citaRepository.existsByMedicoAndFechaAndHora(medico, fecha, hora);
        if (yaReservado) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "❌ Ya existe una cita para ese médico en esa fecha y hora."));
        }

        // ✅ Guardar cita
        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setEspecialidad(dto.getEspecialidad());
        cita.setEstado(dto.getEstado());

        Cita citaGuardada = citaRepository.save(cita);

        // ✅ Armar respuesta
        CitaResponseDTO responseDTO = new CitaResponseDTO();
        responseDTO.setId(citaGuardada.getId());
        responseDTO.setFecha(citaGuardada.getFecha().toString());
        responseDTO.setHora(citaGuardada.getHora().toString());
        responseDTO.setEspecialidad(citaGuardada.getEspecialidad());
        responseDTO.setEstado(citaGuardada.getEstado());
        responseDTO.setNombreMedico(medico.getNombres() + " " + medico.getApellidos());
        responseDTO.setNombrePaciente(paciente.getNombres() + " " + paciente.getApellidos());
        responseDTO.setIdPaciente(paciente.getId());
        responseDTO.setDniPaciente(paciente.getDni());

        return ResponseEntity.ok(responseDTO);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }
}

@GetMapping("/medico/{idMedico}/fecha/{fecha}/cantidad")
public ResponseEntity<Long> contarCitasPorMedicoYFecha(
        @PathVariable Long idMedico,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
    long cantidad = citaRepository.findByMedicoIdAndFecha(idMedico, fecha).size();
    return ResponseEntity.ok(cantidad);
}



    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<CitaResponseDTO>> obtenerCitasPorPaciente(@PathVariable Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        List<Cita> citas = citaRepository.findByPaciente(paciente);

        List<CitaResponseDTO> citasDTO = citas.stream().map(cita -> {
            CitaResponseDTO dto = new CitaResponseDTO();
            dto.setId(cita.getId());
            dto.setEspecialidad(cita.getEspecialidad());
            dto.setFecha(cita.getFecha().toString());
            dto.setHora(cita.getHora().toString());
            dto.setEstado(cita.getEstado());

            Medico medico = cita.getMedico();
            if (medico != null) {
                dto.setNombreMedico(medico.getNombres() + " " + medico.getApellidos());
            } else {
                dto.setNombreMedico("N/D");
            }

            dto.setIdPaciente(paciente.getId());

            return dto;
        }).toList();

        return ResponseEntity.ok(citasDTO);
    }

    @GetMapping("/medico/usuario/{idUsuario}")
public ResponseEntity<?> obtenerCitasPorUsuarioId(@PathVariable Long idUsuario) {
    try {
        Medico medico = medicoRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado para el ID de usuario: " + idUsuario));

        System.out.println("✅ Médico encontrado: " + medico.getNombres() + " (ID: " + medico.getId() + ")");

        var citas = citaService.obtenerCitasPorMedico(medico.getId());

        return ResponseEntity.ok(citas);
    } catch (Exception e) {
        e.printStackTrace(); // 👈 Muy importante ver la traza en consola
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", e.getMessage()));
    }
}


    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        citaService.actualizarEstado(id, estado);
        return ResponseEntity.ok(Map.of("mensaje", "Estado actualizado"));
    }

    @GetMapping("/historial/por-dni/{dni}")
    public ResponseEntity<?> obtenerHistoriasPorDni(@PathVariable String dni) {
        Paciente paciente = pacienteRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        List<HistoriaClinica> historias = historiaClinicaRepository.findByPaciente_Id(paciente.getId());

        return ResponseEntity.ok(historias);
    }

    // ⚠️ CAMBIADO el endpoint para que coincida con Angular
    @PostMapping("/{id}/recetas")
    public ResponseEntity<?> registrarRecetasParaCita(
            @PathVariable Long id,
            @RequestBody List<RecetaInputDTO> recetasDTO) {
        try {
            Cita cita = citaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

            for (RecetaInputDTO dto : recetasDTO) {
                Medicamento medicamento = medicamentoRepository.findById(dto.getIdMedicamento())
                        .orElseThrow(() -> new RuntimeException("Medicamento no encontrado"));

                Receta receta = new Receta();
                receta.setCita(cita);
                receta.setMedicamento(medicamento);
                receta.setCantidad(dto.getCantidad());

                recetaRepository.save(receta);
            }

            return ResponseEntity.ok(Map.of("mensaje", "Recetas registradas correctamente"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }


@GetMapping("/medico/{idMedico}/fecha/{fecha}")
public ResponseEntity<List<CitaResponseDTO>> obtenerCitasPorMedicoYFecha(
        @PathVariable Long idMedico,
        @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

    List<Cita> citas = citaRepository.findByMedicoIdAndFecha(idMedico, fecha);

    List<CitaResponseDTO> dtos = citas.stream().map(c -> {
        CitaResponseDTO dto = new CitaResponseDTO();
        dto.setId(c.getId());
        dto.setFecha(c.getFecha().toString());
        dto.setHora(c.getHora().toString());
        dto.setEspecialidad(c.getEspecialidad());
        dto.setEstado(c.getEstado());
        dto.setNombreMedico(c.getMedico().getNombres() + " " + c.getMedico().getApellidos());
        dto.setNombrePaciente(c.getPaciente().getNombres() + " " + c.getPaciente().getApellidos());
        dto.setDniPaciente(c.getPaciente().getDni());
        dto.setIdPaciente(c.getPaciente().getId());
        return dto;
    }).toList();

    return ResponseEntity.ok(dtos);
}


}
