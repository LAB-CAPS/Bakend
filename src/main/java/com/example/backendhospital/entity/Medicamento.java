package com.example.backendhospital.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "medicamentos")
@Data
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_comercial", nullable = false)
    private String nombreComercial;

    @Column(nullable = false)
    private String presentacion;

    @Column(name = "via_administrativa", nullable = false)
    private String viaAdministrativa;

    @Column(nullable = false)
    private int stock;
}
