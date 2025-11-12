package com.example.backendhospital.controller;

import com.example.backendhospital.service.CargaExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/excel")
@CrossOrigin(origins = "*")
public class ExcelController {

    @Autowired
    private CargaExcelService cargaExcelService;

@PostMapping("/cargar-medicamentos")
public ResponseEntity<?> cargarMedicamentos() {
    cargaExcelService.cargarMedicamentosDesdeExcel();
    return ResponseEntity.ok("Medicamentos cargados correctamente");
}

@PostMapping("/subir-medicamentos")
public ResponseEntity<?> subirYProcesarExcel(@RequestParam("archivo") MultipartFile archivo) {
    try {
        cargaExcelService.cargarMedicamentosDesdeArchivo(archivo);
        return ResponseEntity.ok("Medicamentos cargados correctamente desde el archivo subido");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error al procesar el archivo: " + e.getMessage());
    }
}
}
