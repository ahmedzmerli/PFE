package com.example.GestionUser.entities;

import javax.persistence.*;
@Entity
@Table(name = "pdvs_master_dimii")
//@Table(name = "pdvs_master_dimii", schema = "ccadmin")
public class PdvMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Id
    //    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pdv_master_seq")
    //    @SequenceGenerator(name = "pdv_master_seq", sequenceName = "ccadmin.seq_pdvs_master_dimii", allocationSize = 1)
    //    @Column(name = "id_pdv_master")
    @Column(name = "id_pdv_master")
    private Long id;

    @Column(name = "pdv_name")
    private String nomPdv;
    @Column(name = "address")
    private String adresse;

    @Column(name = "pdv_code")
    private String codePdv;

    public PdvMaster() {
    }

    public PdvMaster(String nomPdv, String adresse, String codePdv) {
        this.nomPdv = nomPdv;
        this.adresse = adresse;
        this.codePdv = codePdv;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
