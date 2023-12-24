package com.administration.controller;

import com.administration.dto.ZoneRequestDTO;
import com.administration.dto.ZoneResponseDTO;
import com.administration.dto.ZoneUpdateDTO;
import com.administration.service.IZoneService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Zone {
    IZoneService IZoneService;

    public Zone(IZoneService IZoneService) {
        this.IZoneService = IZoneService;
    }

    @GetMapping(path="/zones")
    public List<ZoneResponseDTO> allZones()
    {

        return IZoneService.listZones();
    }

    @PostMapping(path="/ajoutezone")
    public ZoneResponseDTO save(@RequestBody ZoneRequestDTO zoneRequestDTO){
        return IZoneService.addZone(zoneRequestDTO);
    }
    @GetMapping(path = "/zone/{idZone}")
    public ZoneResponseDTO getZone(@PathVariable String idZone){

        return IZoneService.getZone(idZone);
    }

    @PutMapping("/update-zone/")
    @ResponseBody
    public void UpdateZoneDTO(@RequestBody ZoneUpdateDTO dto) {
        IZoneService.updateZoneDTO(dto);
    }

    @PutMapping("/affecterDregToZone/{idDreg}/{idZone}")
    public void affecterDregToZone(@PathVariable String idDreg,@PathVariable String idZone){
        IZoneService.affecterDregToZone(idDreg,idZone);
    }
    @PutMapping("/removeDreg/{idDreg}")
    public void affecterDregToZone(@PathVariable String idDreg){
        IZoneService.removeDreg(idDreg);
    }
    @DeleteMapping("/DeleteZone/{idZone}")
    public void DeleteZone(@PathVariable String idZone){
        IZoneService.deleteZone(idZone);
    }

}
