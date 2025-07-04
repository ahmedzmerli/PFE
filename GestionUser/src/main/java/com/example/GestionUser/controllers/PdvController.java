package com.example.GestionUser.controllers;

import com.example.GestionUser.dto.LinkMsisdnsRequest;
import com.example.GestionUser.dto.PdvDTO;
import com.example.GestionUser.services.PdvService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
@RestController
@RequestMapping("/api/v1/pdv")
@RequiredArgsConstructor
public class PdvController {

    private final PdvService service;

    @PostMapping
    @PreAuthorize("hasAuthority('pdv.create')")
    public void create(@RequestBody PdvDTO dto, Principal connectedUser) {
        service.addPdv(dto, connectedUser.getName());
    }

    @DeleteMapping("/{msisdn}")
    @PreAuthorize("hasAuthority('pdv.delete')")
    public void delete(@PathVariable String msisdn, Principal connectedUser) {
        service.deletePdv(msisdn, connectedUser.getName());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('pdv.read')")
    public List<PdvDTO> listAll() {
        return service.listAll();
    }


    @PostMapping("/{idPdvMaster}/add-msisdn")
    @PreAuthorize("hasAuthority('pdv.update')")
    public void addMsisdn(@PathVariable Long idPdvMaster,
                          @RequestParam String msisdn,
                          Principal connectedUser) {
        service.addMsisdnToExistingPdv(idPdvMaster, msisdn, connectedUser.getName());
    }


    @DeleteMapping("/{idPdvMaster}/remove-msisdn")
    @PreAuthorize("hasAuthority('pdv.update')")
    public void removeMsisdn(@PathVariable Long idPdvMaster,
                             @RequestParam String msisdn,
                             Principal connectedUser) {
        service.removeMsisdnFromPdv(idPdvMaster, msisdn, connectedUser.getName());
    }


    @GetMapping("/{idPdvMaster}/msisdns")
    @PreAuthorize("hasAuthority('pdv.read')")
    public List<String> listMsisdns(@PathVariable Long idPdvMaster) {
        return service.listMsisdnsByPdv(idPdvMaster);
    }


    @PutMapping("/{idPdvMaster}")
    @PreAuthorize("hasAuthority('pdv.update')")
    public void update(@PathVariable Long idPdvMaster,
                       @RequestBody PdvDTO dto,
                       Principal connectedUser) {
        service.updatePdv(idPdvMaster, dto, connectedUser.getName());
    }


    @PostMapping("/link-msisdns")
    @PreAuthorize("hasAuthority('pdv.update')")
    public void linkExistingMsisdns(@RequestBody LinkMsisdnsRequest request, Principal connectedUser) {
        service.linkExistingMsisdnsToNewPdv(request.getMsisdns(), request.getPdv(), connectedUser.getName());
    }

}
