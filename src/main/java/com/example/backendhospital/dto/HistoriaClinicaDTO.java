package com.example.backendhospital.dto;


public class HistoriaClinicaDTO {
    private String dniPaciente;
    public String getDniPaciente() {
        return dniPaciente;
    }
    private Long idMedico;
    private String diagnostico;
    private String notas;
   private String medicamentos;
    
    public String getMedicamentos() {
    return medicamentos;
}

public void setMedicamentos(String medicamentos) {
    this.medicamentos = medicamentos;
}
    public void setDniPaciente(String dniPaciente) {
        this.dniPaciente = dniPaciente;
    }
    public Long getIdMedico() {
        return idMedico;
    }
    public void setIdMedico(Long idMedico) {
        this.idMedico = idMedico;
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

    
}
