package com.example.backendhospital.dto;

import lombok.Data;

@Data
public class RecetaInputDTO {
    private Long idCita;
    private Long idMedicamento;
    private int cantidad;
}
