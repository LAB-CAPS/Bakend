package com.example.backendhospital.repository;

import com.example.backendhospital.entity.HistoriaClinica;
import com.example.backendhospital.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Integer> {

    List<HistoriaClinica> findByPaciente(Paciente paciente);

    List<HistoriaClinica> findByPaciente_Id(Long idPaciente); 
    
}
