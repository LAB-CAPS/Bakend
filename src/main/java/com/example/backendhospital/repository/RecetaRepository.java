package com.example.backendhospital.repository;

import com.example.backendhospital.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RecetaRepository extends JpaRepository<Receta, Long> {
    List<Receta> findByCitaId(Long citaId);
    List<Receta> findByCita_Paciente_Id(Long idPaciente);
    List<Receta> findByEstadoAndFechaDespachoBefore(String estado, LocalDateTime fechaDespacho);

    

}
