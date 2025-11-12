package com.example.backendhospital.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "historias_clinicas")
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_medico")
    private Medico medico;

    private LocalDate fecha;

    private String diagnostico;

    @Lob
    private String notas; // Observaciones generales

    @Lob
    private String medicamentos; // Campo para guardar las medicinas recetadas

   
}
