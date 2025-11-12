package com.example.backendhospital.controller;

import com.example.backendhospital.dto.ActualizarEstadoDTO;
import com.example.backendhospital.dto.RecetaAgrupadaDTO;
import com.example.backendhospital.dto.RecetaDTO;
import com.example.backendhospital.entity.Receta;
import com.example.backendhospital.repository.RecetaRepository;
import com.example.backendhospital.service.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


@RestController
@RequestMapping("/api/farmacia")
@CrossOrigin(origins = "http://localhost:4200")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    @Autowired
    private RecetaRepository recetaRepository; // ✅ Inyección agregada

    @GetMapping("/recetas")
    public List<RecetaDTO> obtenerRecetas() {
        return recetaService.obtenerRecetasParaFarmacia();
    }

    @PostMapping("/recetas")
    public void registrarReceta(@RequestBody Receta receta) {
        recetaRepository.save(receta);
    }


@GetMapping("/recetas/paciente/{idPaciente}")
public List<RecetaDTO> obtenerRecetasPorPaciente(@PathVariable Long idPaciente) {
    return recetaService.obtenerRecetasPorPaciente(idPaciente);
}

@PutMapping("/recetas/{id}/estado")
public ResponseEntity<?> actualizarEstado(@PathVariable Long id, @RequestBody ActualizarEstadoDTO dto) {
    try {
        recetaService.actualizarEstadoReceta(id, dto.getEstado());
        return ResponseEntity.ok().build();
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Receta no encontrada");
    }
}

@GetMapping("/recetas/paciente/agrupado/{idPaciente}")
public ResponseEntity<List<RecetaAgrupadaDTO>> obtenerRecetasAgrupadas(@PathVariable Long idPaciente) {
    List<Receta> recetas = recetaRepository.findByCita_Paciente_Id(idPaciente);

    Map<Long, RecetaAgrupadaDTO> agrupadas = new HashMap<>();
    Map<Long, List<Receta>> recetasPorCita = new HashMap<>();

    // Agrupar las recetas por ID de cita
    for (Receta r : recetas) {
        Long idCita = r.getCita().getId();
        recetasPorCita.computeIfAbsent(idCita, k -> new ArrayList<>()).add(r);
    }

    // Procesar cada grupo
    for (Map.Entry<Long, List<Receta>> entry : recetasPorCita.entrySet()) {
        Long idCita = entry.getKey();
        List<Receta> recetasCita = entry.getValue();

        RecetaAgrupadaDTO dto = new RecetaAgrupadaDTO();
        dto.setIdCita(idCita);
        dto.setNombrePaciente(recetasCita.get(0).getCita().getPaciente().getUsuario().getNombreCompleto());
        dto.setNombreMedico(recetasCita.get(0).getCita().getMedico().getUsuario().getNombreCompleto());
        dto.setFechaCita(recetasCita.get(0).getCita().getFecha().toString());

        // ✅ Agregar la especialidad del médico
        if (recetasCita.get(0).getCita().getMedico().getEspecialidad() != null) {
            dto.setEspecialidad(recetasCita.get(0).getCita().getMedico().getEspecialidad().getNombre());
        } else {
            dto.setEspecialidad("Sin especialidad");
        }

        List<RecetaAgrupadaDTO.MedicamentoDTO> meds = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        boolean todasDespachadas = true;

        for (Receta r : recetasCita) {
            RecetaAgrupadaDTO.MedicamentoDTO med = new RecetaAgrupadaDTO.MedicamentoDTO();
            med.setNombre(r.getMedicamento().getNombreComercial());
            med.setPresentacion(r.getMedicamento().getPresentacion());
            med.setCantidad(r.getCantidad());
            meds.add(med);
            ids.add(r.getId());

            // Verificar estado real de la receta
            if (!"DESPACHADO".equalsIgnoreCase(r.getEstado())) {
                todasDespachadas = false;
            }
        }

        dto.setMedicamentos(meds);
        dto.setIdRecetas(ids);
        dto.setEstado(todasDespachadas ? "DESPACHADO" : "EN_ESPERA");

        agrupadas.put(idCita, dto);
    }

    return ResponseEntity.ok(new ArrayList<>(agrupadas.values()));
}



@GetMapping("/recetas/agrupado")
public ResponseEntity<List<RecetaAgrupadaDTO>> obtenerTodasLasRecetasAgrupadas() {
    List<Receta> recetas = recetaRepository.findAll(); // o uno optimizado
    Map<Long, RecetaAgrupadaDTO> agrupadas = new HashMap<>();

    for (Receta r : recetas) {
        Long idCita = r.getCita().getId();

        agrupadas.computeIfAbsent(idCita, k -> {
            RecetaAgrupadaDTO dto = new RecetaAgrupadaDTO();
            dto.setIdCita(idCita);
            dto.setNombrePaciente(r.getCita().getPaciente().getUsuario().getNombreCompleto());
            dto.setNombreMedico(r.getCita().getMedico().getUsuario().getNombreCompleto());
            dto.setFechaCita(r.getCita().getFecha().toString());
            dto.setMedicamentos(new ArrayList<>());
            dto.setIdRecetas(new ArrayList<>()); // NUEVO
            return dto;
        });

        RecetaAgrupadaDTO dto = agrupadas.get(idCita);

        // Agregar medicamento
        RecetaAgrupadaDTO.MedicamentoDTO med = new RecetaAgrupadaDTO.MedicamentoDTO();
        med.setNombre(r.getMedicamento().getNombreComercial());
        med.setPresentacion(r.getMedicamento().getPresentacion());
        med.setCantidad(r.getCantidad());
        dto.getMedicamentos().add(med);

        // Agregar ID de receta
        dto.getIdRecetas().add(r.getId());
    }

    // 🔁 Verificar si TODAS las recetas de cada grupo están despachadas
    for (RecetaAgrupadaDTO dto : agrupadas.values()) {
        List<Long> ids = dto.getIdRecetas();
        boolean todasDespachadas = recetas.stream()
            .filter(r -> ids.contains(r.getId()))
            .allMatch(r -> "DESPACHADO".equalsIgnoreCase(r.getEstado()));
        dto.setEstado(todasDespachadas ? "DESPACHADO" : "EN_ESPERA");
    }

    return ResponseEntity.ok(new ArrayList<>(agrupadas.values()));
}

@PutMapping("/recetas/estado-multiple")
public ResponseEntity<?> actualizarEstadoMultiple(@RequestBody Map<String, Object> payload) {
    List<Integer> ids = (List<Integer>) payload.get("ids");
    String estado = (String) payload.get("estado");

    if (ids == null || estado == null) {
        return ResponseEntity.badRequest().body("Faltan datos");
    }

    for (Integer id : ids) {
        recetaService.actualizarEstadoReceta(Long.valueOf(id), estado); // ✅ aquí se guarda la fecha correctamente
    }

    return ResponseEntity.ok().build();
}

}
