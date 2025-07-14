package com.example.GestionUser.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
//@Table(name = "pdv_dimii", schema = "ccadmin")
public class Pdv {

    @Id
    private String msisdn;

    public Pdv() {
    }

    public Pdv(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
}
