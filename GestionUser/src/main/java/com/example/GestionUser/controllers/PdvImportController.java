package com.example.GestionUser.controllers;

import com.example.GestionUser.services.PdvImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/pdvs")
public class PdvImportController {

    private final PdvImportService pdvImportService;

    public PdvImportController(PdvImportService pdvImportService) {
        this.pdvImportService = pdvImportService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importPdvs(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Le fichier est vide.");
            }
            pdvImportService.importExcel(file);
            return ResponseEntity.ok("Import réussi.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors de l'import : " + e.getMessage());
        }
    }
}
