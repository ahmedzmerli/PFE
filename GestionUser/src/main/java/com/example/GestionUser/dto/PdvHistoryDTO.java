package com.example.GestionUser.dto;

import java.time.LocalDateTime;

public class PdvHistoryDTO {

    private Long pdvMasterId;
    private String username;
    private String actionType;
    private LocalDateTime dateAction;

    public PdvHistoryDTO() {
    }

    public PdvHistoryDTO(Long pdvMasterId, String username, String actionType, LocalDateTime dateAction) {
        this.pdvMasterId = pdvMasterId;
        this.username = username;
        this.actionType = actionType;
        this.dateAction = dateAction;
    }

    public Long getPdvMasterId() {
        return pdvMasterId;
    }

    public void setPdvMasterId(Long pdvMasterId) {
        this.pdvMasterId = pdvMasterId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public LocalDateTime getDateAction() {
        return dateAction;
    }

    public void setDateAction(LocalDateTime dateAction) {
        this.dateAction = dateAction;
    }
}
