package com.example.backendhospital.service;

import com.example.backendhospital.dto.RecetaAgrupadaDTO;
import com.example.backendhospital.dto.RecetaDTO;
import com.example.backendhospital.entity.Receta;

import java.util.List;

public interface RecetaService {
    List<RecetaDTO> obtenerRecetasParaFarmacia();
    void guardarReceta(Receta receta); // ✅ método nuevo
    void registrarRecetas(Long idCita, List<RecetaDTO> recetas);
    List<RecetaDTO> obtenerRecetasPorPaciente(Long idPaciente);
 void actualizarEstadoReceta(Long id, String nuevoEstado);
 List<RecetaAgrupadaDTO> obtenerRecetasAgrupadas();

 
}
