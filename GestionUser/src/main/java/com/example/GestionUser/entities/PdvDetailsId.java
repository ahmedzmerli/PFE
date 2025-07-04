package com.example.GestionUser.entities;


import java.io.Serializable;
import java.util.Objects;

public class PdvDetailsId implements Serializable {
    private String msisdn;
    private Long pdvMasterId;

    public PdvDetailsId() {
    }

    public PdvDetailsId(String msisdn, Long pdvMasterId) {
        this.msisdn = msisdn;
        this.pdvMasterId = pdvMasterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PdvDetailsId)) return false;
        PdvDetailsId that = (PdvDetailsId) o;
        return Objects.equals(msisdn, that.msisdn) &&
                Objects.equals(pdvMasterId, that.pdvMasterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(msisdn, pdvMasterId);
    }
}
