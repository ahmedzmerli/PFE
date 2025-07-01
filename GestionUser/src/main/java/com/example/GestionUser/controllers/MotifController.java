//package com.example.GestionUser.controllers;
//
//import com.example.GestionUser.entities.Motif;
//import com.example.GestionUser.repositories.MotifRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/motifs")
//@RequiredArgsConstructor
//public class MotifController {
//
//    private final MotifRepository repository;
//
//    @GetMapping
//    @PreAuthorize("hasAuthority('motif.read')")
//    public List<Motif> getAll() {
//        return repository.findAll();
//    }
//}
