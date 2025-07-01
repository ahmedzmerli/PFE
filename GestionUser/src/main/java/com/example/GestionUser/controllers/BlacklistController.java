package com.example.GestionUser.controllers;




import com.example.GestionUser.dto.BlacklistRequest;
import com.example.GestionUser.entities.BlHistory;
import com.example.GestionUser.repositories.BlHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.GestionUser.dto.BlacklistDTO;
import com.example.GestionUser.services.BlacklistService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/blacklist")
@RequiredArgsConstructor
public class BlacklistController {

    private final BlacklistService service;
    private final BlHistoryRepository repository;
    @GetMapping
    @PreAuthorize("hasAuthority('blacklist.read')")
    public List<BlacklistDTO> getAll() {
        return service.getAll();
    }

//    @PostMapping("/toggle/{id}")
//    @PreAuthorize("hasAuthority('blacklist.toggle.create')")
//    public void toggleStatus(@PathVariable Long id , @AuthenticationPrincipal UserDetails currentUser) {
//        service.toggleBlacklist(id , currentUser.getUsername());
//    }



//    @PostMapping
//    @PreAuthorize("hasAuthority('blacklist.create')")
//    public BlacklistDTO blacklistMsisdn(@RequestBody BlacklistDTO dto,
//                                        @AuthenticationPrincipal UserDetails currentUser) {
//
//        LocalDateTime now = LocalDateTime.now();
//
//        BlHistory entity = new BlHistory();
//        entity.setMsisdn(dto.getMsisdn());
//        entity.setMotif(dto.getMotif());
//        entity.setStatutBl("BLACKLISTED");
//        entity.setUsername(currentUser.getUsername());
//        entity.setDateDebut(dto.getDateDebut());
//        entity.setDateFin(dto.getDateFin());
//        entity.setDateAction(now);
//        entity.setHourBl(now.toLocalTime().toString());
//        entity.setTypeBlack("MANUAL");
//        entity.setOffre(dto.getOffre() != null ? dto.getOffre() : "N/A");
//        entity.setSegment(dto.getSegment() != null ? dto.getSegment() : "N/A");
//        entity.setTypeClient(dto.getTypeClient() != null ? dto.getTypeClient() : "N/A");
//        entity.setTmcode("N/A");
//        entity.setDateLastBlacklist(null); // si autorisé
//
//
//
//
//        BlHistory saved = repository.save(entity);
//
//        return service.toDTO(saved);
//    }

    @PostMapping
    @PreAuthorize("hasAuthority('blacklist.create')")
    public BlacklistDTO blacklist(@RequestBody BlacklistRequest request,
                                  @AuthenticationPrincipal UserDetails currentUser) {
        // Validation simple
        if (request.getDuree() != 1 && request.getDuree() != 3 &&
                request.getDuree() != 7 && request.getDuree() != 30) {
            throw new IllegalArgumentException("Durée invalide. Valeurs autorisées : 1, 3, 7, 30 jours.");
        }

        BlHistory entity = service.blacklistMsisdn(
                request.getMsisdn(),
                request.getMotif(),
                currentUser.getUsername(),
                request.getDuree()
        );
        return service.toDTO(entity);
    }



    @PostMapping("/whitelist")
    @PreAuthorize("hasAuthority('blacklist.create')")
    public BlacklistDTO whitelist(@RequestBody BlacklistRequest request,
                                  @AuthenticationPrincipal UserDetails currentUser) {
        BlHistory entity = service.whitelistMsisdn(
                request.getMsisdn(),
                currentUser.getUsername()
        );
        return service.toDTO(entity);
    }



}
