package com.example.GestionUser.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.GestionUser.dto.BlHistoryDTO;
import com.example.GestionUser.entities.BlHistory;
import com.example.GestionUser.repositories.BlHistoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class BlHistoryService {

    private final BlHistoryRepository repository;

    public List<BlHistoryDTO> search(String msisdn, LocalDateTime start, LocalDateTime end) {
        LocalDateTime now = LocalDateTime.now();

        if (start == null || end == null) {
            // Si aucune période n’est fournie, on prend les 30 derniers jours
            end = now;
            start = now.minusDays(30);
        }

        return repository.searchHistory(msisdn, start, end)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private BlHistoryDTO toDTO(BlHistory b) {
        BlHistoryDTO dto = new BlHistoryDTO();
        dto.setMsisdn(b.getMsisdn());
        dto.setUsername(b.getUsername());
        dto.setMotif(b.getMotif());
        dto.setStatut(b.getStatutBl());
        dto.setOffre(b.getOffre());
        dto.setSegment(b.getSegment());
        dto.setTypeClient(b.getTypeClient());
        dto.setDateAction(b.getDateAction());
        dto.setStartDate(b.getDateDebut());
        dto.setEndDate(b.getDateFin());
        dto.setTypeBlack(b.getTypeBlack());

        return dto;
    }

}
