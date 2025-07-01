package com.example.GestionUser.controllers;




import com.example.GestionUser.entities.BlHistory;
import com.example.GestionUser.handler.BusinessErrorCodes;
import com.example.GestionUser.handler.BusinessException;
import com.example.GestionUser.services.BlHistoryProcedureService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.GestionUser.dto.BlHistoryDTO;
import com.example.GestionUser.services.BlHistoryService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/blhistory")
@RequiredArgsConstructor
public class BlHistoryController {

    private final BlHistoryService service;
    private final BlHistoryProcedureService procedureService;

//    @GetMapping
//    public List<BlHistoryDTO> searchHistory(
//            @RequestParam String msisdn,
//            @RequestParam(required = false)
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
//            @RequestParam(required = false)
//            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
//
//        // Validation msisdn obligatoire
//        if (msisdn == null || msisdn.isBlank()) {
//            throw new IllegalArgumentException("MSISDN est requis");
//        }
//
//        return service.search(msisdn, start, end);
//    }
//

    @GetMapping
    @PreAuthorize("hasAuthority('blhistory.read')")
    public List<BlHistoryDTO> searchHistory(
            @RequestParam String msisdn,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        if (msisdn == null || msisdn.trim().isEmpty()) {
            throw new BusinessException(BusinessErrorCodes.BL_HISTORY_MSISDN_REQUIRED);
        }

        return service.search(msisdn, start, end);
    }


    @GetMapping("/proc")
    @PreAuthorize("hasAuthority('blhistory.read')")
    public List<BlHistoryDTO> getHistoryByMsisdnProc(
            @RequestParam String msisdn,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        if (msisdn == null || msisdn.trim().isEmpty()) {
            throw new BusinessException(BusinessErrorCodes.BL_HISTORY_MSISDN_REQUIRED);
        }

        // Logique de période :
        LocalDateTime now = LocalDateTime.now();
        if (start == null && end == null) {
            end = now;
            start = now.minusDays(30);
        } else if (start != null && end == null) {
            end = start.plusDays(30);
        } else if (start == null && end != null) {
            start = end.minusDays(30);
        }
        // Sinon : période fournie

        List<BlHistory> list = procedureService.getByMsisdnUsingProcedure(msisdn);

        // Filtre côté Java (car la procédure stockée ne filtre pas par période)
        final LocalDateTime startFinal = start;
        final LocalDateTime endFinal = end;
        List<BlHistoryDTO> dtos = list.stream()
                .filter(bh -> {
                    LocalDateTime date = bh.getDateAction();
                    // Vérification null pour éviter NullPointerException
                    return date != null && (date.isEqual(startFinal) || date.isAfter(startFinal)) && date.isBefore(endFinal);
                })
                .map(BlHistoryDTO::fromEntity)
                .collect(Collectors.toList());

        return dtos;
    }



    @GetMapping("/proc2")
    @PreAuthorize("hasAuthority('blhistory.read')")
    public List<BlHistoryDTO> getHistoryByMsisdnProc(@RequestParam String msisdn) {
        if (msisdn == null || msisdn.trim().isEmpty()) {
            throw new BusinessException(BusinessErrorCodes.BL_HISTORY_MSISDN_REQUIRED);
        }
        List<BlHistory> list = procedureService.getByMsisdnUsingProcedure(msisdn);
        return list.stream()
                .map(BlHistoryDTO::fromEntity)
                .collect(Collectors.toList());
    }





}

