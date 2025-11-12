package com.example.backendhospital.controller;
import java.util.Map;
import com.example.backendhospital.entity.Medicamento;
import com.example.backendhospital.repository.MedicamentoRepository;
import com.example.backendhospital.service.CargaExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
@CrossOrigin(origins = "*")
public class MedicamentoController {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private CargaExcelService cargaExcelService;

    @GetMapping
    public List<Medicamento> listar() {
        return medicamentoRepository.findAll();
    }

    @GetMapping("/cargar")
public String cargarMedicamentosDesdeExcel() {
    try {
        cargaExcelService.cargarMedicamentosDesdeExcel(); // ✅ sin argumento
        return "Medicamentos cargados correctamente desde el Excel.";
    } catch (Exception e) {
        return "Error al cargar medicamentos: " + e.getMessage();
    }
}
@PutMapping("/{id}/aumentar-stock")
public ResponseEntity<Map<String, String>> aumentarStock(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
    int cantidad = body.get("cantidad");
    Medicamento medicamento = medicamentoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Medicamento no encontrado"));
    medicamento.setStock(medicamento.getStock() + cantidad);
    medicamentoRepository.save(medicamento);

    return ResponseEntity.ok(Map.of("mensaje", "Stock actualizado correctamente"));
}

}
