package com.administration.controller;

import com.administration.dto.EttRequestDTO;
import com.administration.dto.EttResponseDTO;
import com.administration.dto.EttUpdateDTO;
import com.administration.service.IEttService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EttController {
    IEttService IEttService;
    public EttController(IEttService IEttService) {

        this.IEttService = IEttService;
    }
    @GetMapping(path="/etts")
    public List<EttResponseDTO> allEtts()
    {

        return IEttService.listEtts();
    }

    @PostMapping(path="/ajouteett")
    public EttResponseDTO save(@RequestBody EttRequestDTO ettRequestDTO){
        return IEttService.addEtt(ettRequestDTO);
    }
    @GetMapping(path = "/ett/{idEtt}")
    public EttResponseDTO getEtt(@PathVariable String idEtt){

        return IEttService.getEtt(idEtt);
    }

    @PutMapping("/update-ett/")
    @ResponseBody
    public void UpdateEttDTO(@RequestBody EttUpdateDTO dto) {
        IEttService.updateEttDTO(dto);
    }

    @PutMapping("/affecterEttToZone/{idZone}/{idEtt}")
    public void affecterEttToZone(@PathVariable String idZone,@PathVariable String idEtt){
        IEttService.affecterEttToZone(idEtt,idZone);
    }
    @PutMapping("/affecterEttToDregional/{idEtt}/{idDreg}")
    public void affecterEttToDreg(@PathVariable String idEtt,@PathVariable String idDreg){
        IEttService.affecterEttToDreg(idEtt,idDreg);
    }

    @PutMapping("/removeZone/{idZone}")
    public void removeZone(@PathVariable String idZone){
        IEttService.removeZone(idZone);
    }

    @DeleteMapping("/DeleteEtt/{idEtt}")
    public void DeleteEtt(@PathVariable String idEtt){
        IEttService.deleteEtt(idEtt);
    }

    @GetMapping(path = "/ettbydr/{drId}")
    public List<EttResponseDTO> getEttbyDr(@PathVariable String drId){

        return IEttService.getEttsByDrId(drId);
    }
}
