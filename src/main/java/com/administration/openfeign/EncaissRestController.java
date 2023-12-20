package com.administration.openfeign;

import com.administration.dto.EncaissResponseDTO;
import com.administration.dto.EncaissUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "microfacture")
public interface EncaissRestController {
    @PostMapping("/encaissement")
    ResponseEntity<EncaissResponseDTO> addEncaiss(@RequestBody EncaissResponseDTO encaissResponseDTO);

    @GetMapping("/encaissement/{id}")
    ResponseEntity<EncaissResponseDTO> getEncaissById(@PathVariable String id);

    @GetMapping("/Byfacture/{idFact}")
    ResponseEntity<List<EncaissResponseDTO>> getEncaissementByFacture(@PathVariable String idFact);

    @GetMapping("/Byuser/{idUser}")
    ResponseEntity<List<EncaissResponseDTO>> getEncaissementByUser(@PathVariable String idUser);

    @GetMapping("/Bycaisse/{idCaisse}")
    ResponseEntity<List<EncaissResponseDTO>> getEncaissementByCaisse(@PathVariable String idCaisse);

    @GetMapping("/Allencaissement")
    ResponseEntity<List<EncaissResponseDTO>> getAllEncaissement();

    @GetMapping("/encaissement/searchPageEncaissement")
    List<EncaissResponseDTO> searchPageEncaiss(
            @RequestParam(name = "produit", required = false, defaultValue = "") String produit,
            @RequestParam(name = "identifiant", required = false, defaultValue = "") String identifiant,
            @RequestParam(name = "modePaiement", required = false, defaultValue = "") String modePaiement,
            @RequestParam(name = "typeIdent", required = false, defaultValue = "") String typeIdent,
            @RequestParam(name = "montantEnc", required = false, defaultValue = "") Double montantEnc,
            @RequestParam(name = "refFacture", required = false, defaultValue = "") String refFacture,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    );

    // Add other methods as needed

    @DeleteMapping("/encaissements/delete/{id}")
    ResponseEntity<Void> deleteEncaiss(@PathVariable String id);

    @PostMapping("/affectEncaisseToCaisse/{idEncaiss}/{idCai}")
    ResponseEntity<?> affectEncaisseToCaisse(@PathVariable("idEncaiss") String idEncaiss,
                                             @PathVariable("idCai") String idCai);

    @GetMapping("/encaissements/current-month-for-caisse")
    List<EncaissResponseDTO> getEncaissementsForCaisseInCurrentMonth(@RequestParam String caisseId);

    @PutMapping("/encaissementupdate")
    ResponseEntity<?> updateEncaiss(@RequestBody EncaissUpdateDTO encaissUpdateDTO);

    @GetMapping("/encaissement/thisyear")
    List<EncaissResponseDTO> searchMonthEncaiss();
}
