package com.administration.dto;

import lombok.Data;

import java.util.List;

@Data
public class CaisseResponseDTO {

    private String idCaisse;
    private int numCaise;
    private String f_Actif;
    private UtilisateurUpdateDTO login;
    private EttUpdateDTO cod_ett;
    private List<EncaissResponseDTO> encaissements;
}
