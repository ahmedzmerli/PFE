package com.example.GestionUser.dto;

public class BlacklistRequest {
    private String msisdn;
    private String motif;
//    private String typeClient;
    private int duree; // Durée en jours (1, 3, 7, 30)

    // Getters et Setters
    public String getMsisdn() { return msisdn; }
    public void setMsisdn(String msisdn) { this.msisdn = msisdn; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
//    public String getTypeClient() { return typeClient; }
//    public void setTypeClient(String typeClient) { this.typeClient = typeClient; }
    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }
}
