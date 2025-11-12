package com.example.backendhospital.controller;

import com.example.backendhospital.entity.Especialidad;
import com.example.backendhospital.repository.EspecialidadRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController {

    private final EspecialidadRepository especialidadRepository;

    public EspecialidadController(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }


     @GetMapping
    public List<Especialidad> listar() {
        return especialidadRepository.findAll();
    }
}
