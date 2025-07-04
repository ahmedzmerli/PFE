package com.example.GestionUser.dto;
public class PdvDTO {

    private String msisdn;
    private Long idPdvMaster;
    private String nomPdv;
    private String adresse;
    private String codePdv;

    public PdvDTO() {}

    public PdvDTO(String msisdn, Long idPdvMaster, String nomPdv, String adresse, String codePdv) {
        this.msisdn = msisdn;
        this.idPdvMaster = idPdvMaster;
        this.nomPdv = nomPdv;
        this.adresse = adresse;
        this.codePdv = codePdv;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Long getIdPdvMaster() {
        return idPdvMaster;
    }

    public void setIdPdvMaster(Long idPdvMaster) {
        this.idPdvMaster = idPdvMaster;
    }

    public String getNomPdv() {
        return nomPdv;
    }

    public void setNomPdv(String nomPdv) {
        this.nomPdv = nomPdv;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodePdv() {
        return codePdv;
    }

    public void setCodePdv(String codePdv) {
        this.codePdv = codePdv;
    }
}
