package com.example.backendhospital.config;

import com.example.backendhospital.entity.Especialidad;
import com.example.backendhospital.repository.EspecialidadRepository;
import com.example.backendhospital.service.CargaExcelService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class StartupLoader {

    private final CargaExcelService cargaExcelService;
    private final EspecialidadRepository especialidadRepository;

    public StartupLoader(CargaExcelService cargaExcelService, EspecialidadRepository especialidadRepository) {
        this.cargaExcelService = cargaExcelService;
        this.especialidadRepository = especialidadRepository;
    }

    @PostConstruct
    public void init() {
        cargarEspecialidades();
        // puedes llamar también cargaExcelService si lo necesitas aquí
    }

    private void cargarEspecialidades() {
        if (especialidadRepository.count() == 0) {
            List<String> especialidades = Arrays.asList(
                "Medicina General",
                "Medicina Interna",
                "Pediatría",
                "Ginecología y Obstetricia",
                "Cirugía General",
                "Traumatología y Ortopedia",
                "Dermatología",
                "Cardiología",
                "Neurología",
                "Psiquiatría"
            );

            especialidades.forEach(nombre -> {
                Especialidad e = new Especialidad();
                e.setNombre(nombre);
                especialidadRepository.save(e);
            });

            System.out.println("✅ Especialidades precargadas correctamente.");
        }
    }
}
