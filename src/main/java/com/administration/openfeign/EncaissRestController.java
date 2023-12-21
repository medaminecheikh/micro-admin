package com.administration.openfeign;

import com.administration.dto.EncaissResponseDTO;
import com.administration.dto.EncaissUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "MICROFACTURE")
public interface EncaissRestController {

    @GetMapping("/encaissement/{id}")
    ResponseEntity<EncaissResponseDTO> getEncaissById(@PathVariable String id);
    @GetMapping("/encaissement/forcaisse/{id}")
    ResponseEntity<List<EncaissResponseDTO>> getEncaissForCaisseById(@PathVariable String id);



    @DeleteMapping("/encaissements/delete/{id}")
    ResponseEntity<Void> deleteEncaiss(@PathVariable String id);

    @PostMapping("/affectEncaisseToCaisse/{idEncaiss}/{idCai}")
    ResponseEntity<?> affectEncaisseToCaisse(@PathVariable("idEncaiss") String idEncaiss,
                                             @PathVariable("idCai") String idCai);



}
