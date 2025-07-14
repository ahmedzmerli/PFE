package com.example.GestionUser.controllers;

import com.example.GestionUser.dto.PdvHistoryDTO;
import com.example.GestionUser.entities.PdvHistory;
import com.example.GestionUser.repositories.PdvHistoryRepository;
import com.example.GestionUser.repositories.PdvMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pdv-history")
@RequiredArgsConstructor
public class PdvHistoryController {

    private final PdvHistoryRepository historyRepository;
    private final PdvMasterRepository pdvMasterRepository;


    @GetMapping
    @PreAuthorize("hasAuthority('pdvhistory.read')")
    public List<PdvHistoryDTO> getAll() {
        return historyRepository.findAll().stream().map(p -> {
            PdvHistoryDTO dto = new PdvHistoryDTO();
            dto.setPdvMasterId(p.getPdvMasterId());
            dto.setUsername(p.getUsername());
            dto.setActionType(p.getActionType());
            dto.setDateAction(p.getDateAction());
            // Récupérer le nom du PDV
            dto.setPdvName(
                    p.getPdvMasterId() != null
                            ? pdvMasterRepository.findById(p.getPdvMasterId())
                            .map(m -> m.getNomPdv())
                            .orElse("PDV inconnu")
                            : "PDV inconnu"
            );
            return dto;
        }).collect(Collectors.toList());
    }
}
