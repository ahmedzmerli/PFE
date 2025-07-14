package com.example.GestionUser.entities;

import javax.persistence.*;

@Entity
@Table(name = "pdv_details_dimii")
//@Table(name = "pdv_details_dimii", schema = "ccadmin")
@IdClass(PdvDetailsId.class)
public class PdvDetails {

    @Id
    @Column(name = "msisdn")
    private String msisdn;

    @Id
    @Column(name = "id_pdv_master")
    private Long pdvMasterId;

    public PdvDetails() {
    }

    public PdvDetails(String msisdn, Long pdvMasterId) {
        this.msisdn = msisdn;
        this.pdvMasterId = pdvMasterId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Long getPdvMasterId() {
        return pdvMasterId;
    }

    public void setPdvMasterId(Long pdvMasterId) {
        this.pdvMasterId = pdvMasterId;
    }
}

