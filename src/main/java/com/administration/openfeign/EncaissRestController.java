package com.administration.openfeign;

import com.administration.dto.EncaissResponseDTO;
import com.administration.dto.EncaissUpdateDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@FeignClient(name = "MICROFACTURE")
public interface EncaissRestController {

    @GetMapping("/encaissement/{id}")
    ResponseEntity<EncaissResponseDTO> getEncaissById(@PathVariable String id);

    @GetMapping("/encaissement/forcaisse/{id}")
    @CircuitBreaker(name = "microfacture-forcaisse", fallbackMethod = "fallbackForcaisse")
    ResponseEntity<List<EncaissResponseDTO>> getEncaissForCaisseById(@PathVariable String id);


    @DeleteMapping("/encaissements/delete/{id}")
    ResponseEntity<Void> deleteEncaiss(@PathVariable String id);

    @PostMapping("/affectEncaisseToCaisse/{idEncaiss}/{idCai}")
    ResponseEntity<?> affectEncaisseToCaisse(@PathVariable("idEncaiss") String idEncaiss,
                                             @PathVariable("idCai") String idCai);


    // Fallback method
     default ResponseEntity<List<EncaissResponseDTO>> fallbackForcaisse(String id, Throwable throwable) {
        // Handle the fallback logic here
        // You can log the error or provide a default response
        // For simplicity, returning an empty list in this example
        return ResponseEntity.ok(Collections.emptyList());
    }
}
