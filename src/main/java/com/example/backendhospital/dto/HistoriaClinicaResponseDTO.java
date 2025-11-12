package com.example.backendhospital.dto;

public class HistoriaClinicaResponseDTO {
    private Long id;
    private String fecha;
    private String diagnostico;
    private String notas;
    private String medicamentos;
    private String nombreMedico;
    private String nombrePaciente; // ✅ Nuevo campo

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDiagnostico() {
        return diagnostico;
    }
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getNotas() {
        return notas;
    }
    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getMedicamentos() {
        return medicamentos;
    }
    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos;
    }

    public String getNombreMedico() {
        return nombreMedico;
    }
    public void setNombreMedico(String nombreMedico) {
        this.nombreMedico = nombreMedico;
    }

    // ✅ Getter y Setter para nombrePaciente
    public String getNombrePaciente() {
        return nombrePaciente;
    }
    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }




    
}
