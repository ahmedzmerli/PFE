package com.example.GestionUser.entities.jim;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "jim_dashboard") // facultatif, selon le nom réel de ta table
public class JimDashboard {

    @Id
    @Column(name = "CALL_ID")
    private String callId;

    @Column(name = "HOTLINE")
    private String hotline;

    @Column(name = "NUM_CLIENT")
    private String numClient;

    @Column(name = "TIME_IN_QUEUE")
    private String timeInQueue;

    @Column(name = "FILE_ATT")
    private String fileAtt;

    @Column(name = "DATE_HEURS")
    private LocalDateTime dateHeurs;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "SEGMENT")
    private String segment;

    @Column(name = "CONN_ID")
    private String CONNID;

    @Column(name = "AGENT")
    private String AGENT;

    @Column(name = "DEBUT_APPEL")
    private LocalDateTime DEBUT_APPEL;

    @Column(name = "FIN_APPEL")
    private LocalDateTime FIN_APPEL;

    public JimDashboard() {
    }

    public JimDashboard(String callId, String hotline, String numClient, String timeInQueue,
                        String fileAtt, LocalDateTime dateHeurs, String status, String segment,
                        String CONNID, String AGENT, LocalDateTime DEBUT_APPEL, LocalDateTime FIN_APPEL) {
        this.callId = callId;
        this.hotline = hotline;
        this.numClient = numClient;
        this.timeInQueue = timeInQueue;
        this.fileAtt = fileAtt;
        this.dateHeurs = dateHeurs;
        this.status = status;
        this.segment = segment;
        this.CONNID = CONNID;
        this.AGENT = AGENT;
        this.DEBUT_APPEL = DEBUT_APPEL;
        this.FIN_APPEL = FIN_APPEL;
    }

    // Getters et Setters

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getNumClient() {
        return numClient;
    }

    public void setNumClient(String numClient) {
        this.numClient = numClient;
    }

    public String getTimeInQueue() {
        return timeInQueue;
    }

    public void setTimeInQueue(String timeInQueue) {
        this.timeInQueue = timeInQueue;
    }

    public String getFileAtt() {
        return fileAtt;
    }

    public void setFileAtt(String fileAtt) {
        this.fileAtt = fileAtt;
    }

    public LocalDateTime getDateHeurs() {
        return dateHeurs;
    }

    public void setDateHeurs(LocalDateTime dateHeurs) {
        this.dateHeurs = dateHeurs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getCONNID() {
        return CONNID;
    }

    public void setCONNID(String CONNID) {
        this.CONNID = CONNID;
    }

    public String getAGENT() {
        return AGENT;
    }

    public void setAGENT(String AGENT) {
        this.AGENT = AGENT;
    }

    public LocalDateTime getDEBUT_APPEL() {
        return DEBUT_APPEL;
    }

    public void setDEBUT_APPEL(LocalDateTime DEBUT_APPEL) {
        this.DEBUT_APPEL = DEBUT_APPEL;
    }

    public LocalDateTime getFIN_APPEL() {
        return FIN_APPEL;
    }

    public void setFIN_APPEL(LocalDateTime FIN_APPEL) {
        this.FIN_APPEL = FIN_APPEL;
    }
}
