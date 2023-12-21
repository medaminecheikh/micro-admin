package com.administration.controller;

import com.administration.dto.CaisseRequestDTO;
import com.administration.dto.CaisseResponseDTO;
import com.administration.dto.CaisseUpdateDTO;
import com.administration.service.ICaisseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/caisse")
@Slf4j
public class CaisseController {

    private final ICaisseService caisseService;

    public CaisseController(ICaisseService caisseService) {
        this.caisseService = caisseService;
    }

    @PostMapping("/add")
    public ResponseEntity<CaisseResponseDTO> addCaisse(@RequestBody CaisseRequestDTO caisseRequestDTO) {
        CaisseResponseDTO caisse = caisseService.addCaisse(caisseRequestDTO);
        return new ResponseEntity<>(caisse, HttpStatus.CREATED);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CaisseResponseDTO> getCaisse(@PathVariable String id) {
        log.info("CAISSE BY ID CALLED");
        try {
            CaisseResponseDTO caisse = caisseService.getCaisse(id);
            return new ResponseEntity<>(caisse, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            log.error("Caisse not found for id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            log.error("Error retrieving caisse for id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/getCaisseForEnc/{id}")
    public ResponseEntity<CaisseResponseDTO> getCaisseForEnc(@PathVariable String id) {
        try {
            log.info("CAISSE For Enc CALLED");
            CaisseResponseDTO caisse = caisseService.getCaisseForEn(id);
            return new ResponseEntity<>(caisse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Caisse not found for id: {} And {}", id,e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<CaisseResponseDTO>> listCaisses() {
        List<CaisseResponseDTO> caisses = caisseService.listCaisses();
        return new ResponseEntity<>(caisses, HttpStatus.OK);
    }

    @PutMapping("/update/")
    public ResponseEntity<Void> updateCaisse(@RequestBody CaisseUpdateDTO dto) {

        caisseService.updateCaisseDTO(dto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{idCaisse}/utilisateurs/{idUser}")
    public ResponseEntity<Void> affecterCaisseToUser(@PathVariable String idCaisse, @PathVariable String idUser) {
        caisseService.affecterCaisseToUser(idCaisse, idUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/utilisateurs/{idUser}")
    public ResponseEntity<Void> removeUser(@PathVariable String idUser) {
        caisseService.removeUser(idUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/affect/{idCaisse}/etts/{idEtt}")
    public ResponseEntity<Void> affecterCaisseToEtt(@PathVariable String idCaisse, @PathVariable String idEtt) {
        caisseService.affecterCaisseToEtt(idCaisse, idEtt);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCaisse(@PathVariable String id) {
        caisseService.deleteCaisse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/etts/{id}")
    public ResponseEntity<List<CaisseResponseDTO>> getCaissesByEttId(@PathVariable String id) {
        List<CaisseResponseDTO> caisses = caisseService.getCaissesByEttId(id);
        return new ResponseEntity<>(caisses, HttpStatus.OK);
    }
}
