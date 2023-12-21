package com.administration.service.impl;

import com.administration.dto.CaisseRequestDTO;
import com.administration.dto.CaisseResponseDTO;
import com.administration.dto.CaisseUpdateDTO;
import com.administration.dto.EncaissResponseDTO;
import com.administration.entity.Caisse;
import com.administration.entity.Ett;
import com.administration.entity.Utilisateur;
import com.administration.openfeign.EncaissRestController;
import com.administration.repo.CaisseRepo;
import com.administration.repo.EttRepo;
import com.administration.repo.UtilisateurRepo;
import com.administration.service.ICaisseService;
import com.administration.service.mappers.CaisseMappers;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CaisseServiceImpl implements ICaisseService {

    CaisseRepo caisseRepo;
    CaisseMappers caisseMapper;
    UtilisateurRepo utilisateurRepo;
    EncaissRestController encaissRestController;
    EttRepo ettRepo;

    @Override
    public CaisseResponseDTO addCaisse(CaisseRequestDTO caisseRequestDTO) {
        Caisse caisse = caisseMapper.CaisseRequestDTOCaisse(caisseRequestDTO);
        caisse.setIdCaisse(UUID.randomUUID().toString());
        caisseRepo.save(caisse);
        return caisseMapper.CaisseTOCaisseResponseDTO(caisse);
    }

    @Override
    public CaisseResponseDTO getCaisse(String id) {
        try {
            Caisse caisse = caisseRepo.findById(id).orElseThrow();
            List<EncaissResponseDTO> encaissResponseDTOS = encaissRestController.getEncaissForCaisseById(id).getBody();
            // Ensure that the encaissements list is initialized
            if (caisse.getEncaissements() == null) {
                caisse.setEncaissements(new ArrayList<>());
            }

            if (encaissResponseDTOS != null && !encaissResponseDTOS.isEmpty()) {
                caisse.getEncaissements().addAll(encaissResponseDTOS);
            }

            return caisseMapper.CaisseTOCaisseResponseDTO(caisse);
        } catch (Exception e) {
            log.error("Error retrieving caisse for id {}: {}", id, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CaisseResponseDTO> listCaisses() {
        List<Caisse> caisses = caisseRepo.findAll();
        return caisses.stream().map(caisse -> {
                    List<EncaissResponseDTO> encaissResponseDTOS = encaissRestController.getEncaissForCaisseById(caisse.getIdCaisse()).getBody();
                    // Ensure that the encaissements list is initialized
                    if (caisse.getEncaissements() == null) {
                        caisse.setEncaissements(new ArrayList<>());
                    }

                    if (encaissResponseDTOS != null && !encaissResponseDTOS.isEmpty()) {
                        caisse.getEncaissements().addAll(encaissResponseDTOS);
                    }
                    return caisseMapper.CaisseTOCaisseResponseDTO(caisse);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void updateCaisseDTO(CaisseUpdateDTO dto) {
        Caisse caisse = caisseRepo.findById(dto.getIdCaisse()).get();
        caisseMapper.updateCaisseFromDto(dto, caisse);
        caisseRepo.save(caisse);
    }

    @Override
    public void affecterCaisseToUser(String idCaisse, String idUser) {
        Caisse caisse = caisseRepo.findById(idCaisse).orElse(null);
        Utilisateur utilisateur = utilisateurRepo.findById(idUser).orElse(null);

        if (caisse != null && utilisateur != null) {
            utilisateur.setCaisse(caisse);
            utilisateurRepo.save(utilisateur);
        } else {
            // Throw an exception or handle the error accordingly
            throw new IllegalArgumentException("Invalid caisse or utilisateur ID");
            // Or you can log an error message and perform alternative actions
            // logger.error("Invalid caisse or utilisateur ID");
            // performAlternativeAction();
        }
    }

    @Override
    public void removeUser(String idUser) {

        Utilisateur utilisateur = utilisateurRepo.findById(idUser).get();
        utilisateur.setCaisse(null);
        utilisateurRepo.save(utilisateur);
    }

    @Override
    public void affecterCaisseToEtt(String idCaisse, String idEtt) {
        Caisse caisse = caisseRepo.findById(idCaisse).get();
        Ett ett = ettRepo.findById(idEtt).get();
        caisse.setCod_ett(ett);
        caisseRepo.save(caisse);
    }

    @Override
    public void deleteCaisse(String idCaisse) {
        Optional<Caisse> caisseOptional = caisseRepo.findById(idCaisse);
        if (caisseOptional.isPresent()) {
            Caisse caisse = caisseOptional.get();

            // Remove the association from the associated Utilisateur entity
            Utilisateur utilisateur = caisse.getLogin();
            if (utilisateur != null) {
                removeUser(utilisateur.getIdUser());
            }
            caisseRepo.deleteById(idCaisse);
        }
    }

    @Override
    public List<CaisseResponseDTO> getCaissesByEttId(String Id) {
        Ett ett = ettRepo.findById(Id)
                .orElseThrow(() -> new EntityNotFoundException("Ett not found with id: " + Id));

        List<Caisse> caisseList = ett.getCaisses();
        return caisseList.stream().map(caisse -> {
                    List<EncaissResponseDTO> encaissResponseDTOS = encaissRestController.getEncaissForCaisseById(caisse.getIdCaisse()).getBody();
                    // Ensure that the encaissements list is initialized
                    if (caisse.getEncaissements() == null) {
                        caisse.setEncaissements(new ArrayList<>());
                    }

                    if (encaissResponseDTOS != null && !encaissResponseDTOS.isEmpty()) {
                        caisse.getEncaissements().addAll(encaissResponseDTOS);
                    }
                    return caisseMapper.CaisseTOCaisseResponseDTO(caisse);
                })
                .collect(Collectors.toList());
    }

    @Override
    public CaisseResponseDTO getCaisseForEn(String id) {

        try {
            Caisse caisse = caisseRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Caisse not found with id: " + id));
            return caisseMapper.CaisseTOCaisseResponseDTO(caisse);
        } catch (Exception e) {
            log.error("Error retrieving caisse for id {}: {}", id, e.getMessage());
            throw new RuntimeException("Error retrieving caisse for id " + id + ": " + e.getMessage());
        }
    }

    public List<EncaissResponseDTO> getEncaissForCaisse(String caisseId) {
        try {
            ResponseEntity<List<EncaissResponseDTO>> responseEntity = encaissRestController.getEncaissForCaisseById(caisseId);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                List<EncaissResponseDTO> encaissResponseDTOS = responseEntity.getBody();

                if (encaissResponseDTOS != null && !encaissResponseDTOS.isEmpty()) {
                    return encaissResponseDTOS;
                } else {
                    // Handle the case where the response body is null or empty
                    log.warn("EncaissResponseDTO list is null or empty for caisseId: {}", caisseId);
                    return Collections.emptyList();
                }
            } else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
                // Caisse not found, return an empty list
                return Collections.emptyList();
            } else {
                // Handle non-successful status code
                throw new RuntimeException("Failed to get encaissements for caisse. HTTP Status: " + responseEntity.getStatusCodeValue());
            }
        } catch (FeignException e) {
            // Handle Feign client exceptions (e.g., communication errors)
            // Log the error for debugging purposes
            log.error("Feign client error while getting encaissements for caisse: {}", e.getMessage());
            throw new RuntimeException("Failed to get encaissements for caisse. Error: " + e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            // Log the error for debugging purposes
            log.error("Error getting encaissements for caisse: {}", e.getMessage());
            throw new RuntimeException("Failed to get encaissements for caisse. Error: " + e.getMessage());
        }
    }

}
