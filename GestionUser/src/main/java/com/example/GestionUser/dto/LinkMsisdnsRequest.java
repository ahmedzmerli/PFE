package com.example.GestionUser.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class LinkMsisdnsRequest {
    private List<String> msisdns;
    private PdvDTO pdv;


}

