package com.administration.controller;

import com.administration.dto.DregionalRequestDTO;
import com.administration.dto.DregionalResponseDTO;
import com.administration.dto.DregionalUpdateDTO;
import com.administration.service.IDregService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DregionalController {
    IDregService IDregService;

    public DregionalController(IDregService IDregService) {
        this.IDregService = IDregService;
    }
    @GetMapping(path="/dregionals")
    public List<DregionalResponseDTO> allDregionals()
    {

        return IDregService.listDregionals();
    }

    @PostMapping(path="/ajouteDreg")
    public DregionalResponseDTO save(@RequestBody DregionalRequestDTO dregRequestDTO){
        return IDregService.addDreg(dregRequestDTO);
    }
    @GetMapping(path = "/dreg/{idDregional}")
    public DregionalResponseDTO getDregional(@PathVariable String idDregional){

        return IDregService.getDregional(idDregional);
    }

    @PutMapping("/update-dreg/")
    @ResponseBody
    public void UpdateDregionalDTO(@RequestBody DregionalUpdateDTO dto) {
        IDregService.updateDregionalDTO(dto);
    }

    @GetMapping(path = "/dregbyzone/{idZone}")
    public List<DregionalResponseDTO> getDregionalsByZone(@PathVariable String idZone){

        return IDregService.getDregionalsByZoneId(idZone);
    }
}
