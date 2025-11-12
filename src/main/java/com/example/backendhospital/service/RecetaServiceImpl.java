package com.example.backendhospital.service;
import java.util.Map;
import com.example.backendhospital.dto.RecetaAgrupadaDTO;
import com.example.backendhospital.dto.RecetaAgrupadaDTO.MedicamentoDTO;
import com.example.backendhospital.dto.RecetaDTO;
import com.example.backendhospital.entity.Cita;
import com.example.backendhospital.entity.Medicamento;
import com.example.backendhospital.entity.Receta;
import com.example.backendhospital.repository.CitaRepository;
import com.example.backendhospital.repository.MedicamentoRepository;
import com.example.backendhospital.repository.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import java.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecetaServiceImpl implements RecetaService {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private RecetaRepository recetaRepository;

    @Override
    public List<RecetaDTO> obtenerRecetasParaFarmacia() {
        List<Receta> recetas = recetaRepository.findAll();

        return recetas.stream().map(r -> new RecetaDTO(
            r.getId(), 
                r.getMedicamento().getId(),
                r.getCita().getPaciente().getUsuario().getNombres() + " " + r.getCita().getPaciente().getUsuario().getApellidos(),
                r.getCita().getMedico().getUsuario().getNombres() + " " + r.getCita().getMedico().getUsuario().getApellidos(),
                r.getMedicamento().getNombreComercial(),
                r.getMedicamento().getPresentacion(),
                r.getCantidad(),
                r.getCita().getFecha().toString(),
                r.getEstado(), 
                r.getFechaDespacho() // Estado actual de la receta
        )).collect(Collectors.toList());
    }

    @Override
    public void guardarReceta(Receta receta) {
        recetaRepository.save(receta);
    }

    @Override
    public void registrarRecetas(Long idCita, List<RecetaDTO> recetas) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con ID: " + idCita));

        for (RecetaDTO dto : recetas) {
            Medicamento medicamento = medicamentoRepository.findById(dto.getIdMedicamento())
                    .orElseThrow(() -> new RuntimeException("Medicamento no encontrado con ID: " + dto.getIdMedicamento()));

            Receta receta = new Receta();
            receta.setCita(cita);
            receta.setMedicamento(medicamento);
            receta.setCantidad(dto.getCantidad());
            receta.setEstado("EN_ESPERA"); // Estado inicial por defecto

            recetaRepository.save(receta);
        }
    }

    @Override
    public List<RecetaDTO> obtenerRecetasPorPaciente(Long idPaciente) {
        return recetaRepository.findByCita_Paciente_Id(idPaciente).stream()
                .map(r -> new RecetaDTO(
                    r.getId(), 
                        r.getMedicamento().getId(),
                        r.getCita().getPaciente().getUsuario().getNombres() + " " + r.getCita().getPaciente().getUsuario().getApellidos(),
                        r.getCita().getMedico().getUsuario().getNombres() + " " + r.getCita().getMedico().getUsuario().getApellidos(),
                        r.getMedicamento().getNombreComercial(),
                        r.getMedicamento().getPresentacion(),
                        r.getCantidad(),
                        r.getCita().getFecha().toString(),
                        r.getEstado(), 
                        r.getFechaDespacho()
                )).collect(Collectors.toList());
    }

@Override
public void actualizarEstadoReceta(Long id, String nuevoEstado) {
    Receta receta = recetaRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

    if ("DESPACHADO".equalsIgnoreCase(nuevoEstado) && !"DESPACHADO".equalsIgnoreCase(receta.getEstado())) {
        Medicamento medicamento = receta.getMedicamento();

        int stockActual = medicamento.getStock();
        int cantidadRecetada = receta.getCantidad();

        if (stockActual < cantidadRecetada) {
            throw new RuntimeException("Stock insuficiente para el medicamento: " + medicamento.getNombreComercial());
        }

        medicamento.setStock(stockActual - cantidadRecetada);
        medicamentoRepository.save(medicamento);

        // ✅ Guardar fecha actual del despacho
        receta.setFechaDespacho(LocalDateTime.now());
    }

    receta.setEstado(nuevoEstado);
    recetaRepository.save(receta);
}


@Override
public List<RecetaAgrupadaDTO> obtenerRecetasAgrupadas() {
    List<Receta> recetas = recetaRepository.findAll();

    // Agrupar por ID de cita
    Map<Long, List<Receta>> recetasPorCita = recetas.stream()
        .collect(Collectors.groupingBy(r -> r.getCita().getId()));

    // Transformar en DTOs agrupados
    return recetasPorCita.entrySet().stream().map(entry -> {
        Long idCita = entry.getKey();
        List<Receta> recetasDeLaCita = entry.getValue();

        Receta primera = recetasDeLaCita.get(0); // Usamos la primera para datos comunes

        RecetaAgrupadaDTO dto = new RecetaAgrupadaDTO();
        dto.setIdCita(idCita);
        dto.setNombrePaciente(primera.getCita().getPaciente().getUsuario().getNombres()
            + " " + primera.getCita().getPaciente().getUsuario().getApellidos());
        dto.setNombreMedico(primera.getCita().getMedico().getUsuario().getNombres()
            + " " + primera.getCita().getMedico().getUsuario().getApellidos());
        dto.setFechaCita(primera.getCita().getFecha().toString());

        // Si todas las recetas están despachadas, el estado es DESPACHADO
        boolean todasDespachadas = recetasDeLaCita.stream()
            .allMatch(r -> "DESPACHADO".equalsIgnoreCase(r.getEstado()));
        dto.setEstado(todasDespachadas ? "DESPACHADO" : "EN_ESPERA");

        // Lista de medicamentos
        List<MedicamentoDTO> medicamentos = recetasDeLaCita.stream().map(r -> {
            MedicamentoDTO medDTO = new MedicamentoDTO();
            medDTO.setNombre(r.getMedicamento().getNombreComercial());
            medDTO.setPresentacion(r.getMedicamento().getPresentacion());
            medDTO.setCantidad(r.getCantidad());
            return medDTO;
        }).collect(Collectors.toList());
        dto.setMedicamentos(medicamentos);

        // Lista de IDs de recetas
        List<Long> idRecetas = recetasDeLaCita.stream()
            .map(Receta::getId)
            .collect(Collectors.toList());
        dto.setIdRecetas(idRecetas);

        return dto;
    }).collect(Collectors.toList());
}


@Scheduled(fixedRate = 3000) // ✅ Cada 3 segundos revisa si hay recetas para eliminar
public void eliminarRecetasDespachadasAntiguas() {
    LocalDateTime hace10Segundos = LocalDateTime.now().minusSeconds(10);
    List<Receta> antiguas = recetaRepository.findByEstadoAndFechaDespachoBefore("DESPACHADO", hace10Segundos);

    if (!antiguas.isEmpty()) {
        recetaRepository.deleteAll(antiguas);
        System.out.println("🧹 Recetas eliminadas después de 10 segundos: " + antiguas.size());
    }
}

}
