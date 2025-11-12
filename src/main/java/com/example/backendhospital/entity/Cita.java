package com.example.backendhospital.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "citas")
public class Cita {

   @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    private String especialidad;

    @ManyToOne
    @JoinColumn(name = "id_paciente")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_medico")
    private Medico medico;

    private LocalDate fecha;
    private LocalTime hora;

    private String estado; 
    // EJEMPLO: PENDIENTE, ATENDIDA, CANCELADA
}
