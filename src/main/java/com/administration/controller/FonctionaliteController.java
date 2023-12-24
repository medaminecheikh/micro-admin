package com.administration.controller;

import com.administration.dto.FoncRequestDTO;
import com.administration.dto.FoncResponseDTO;
import com.administration.dto.FoncUpdateDTO;
import com.administration.service.IFoncService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FonctionaliteController {

    IFoncService IFoncService;

    public FonctionaliteController(IFoncService IFoncService) {
        this.IFoncService = IFoncService;
    }

    @GetMapping(path="/foncs")
    public List<FoncResponseDTO> allFoncs()
    {

        return IFoncService.listFoncs();
    }

    @PostMapping(path="/ajoutefonc")
    public FoncResponseDTO save(@RequestBody FoncRequestDTO foncRequestDTO){
        return IFoncService.addFonc(foncRequestDTO);
    }
    @PostMapping(path="/ajoutesousfonc")
    public FoncResponseDTO savesous(@RequestBody FoncRequestDTO foncRequestDTO){
        return IFoncService.addsousFonc(foncRequestDTO);
    }
    @GetMapping(path = "/fonc/{idFonc}")
    public FoncResponseDTO getFonc(@PathVariable String idFonc){

        return IFoncService.getFonc(idFonc);
    }
    @PutMapping("/update-fonc/")
    @ResponseBody
    public void UpdateFoncDTO(@RequestBody FoncUpdateDTO dto) {
        IFoncService.updateFoncDTO(dto);
    }

    @PutMapping("/affecterModelToFonc/{idModel}/{idFonc}")
    public void affecterModelToProfile(@PathVariable String idModel,@PathVariable String idFonc){
        IFoncService.affecterModelToFonc(idModel,idFonc);
    }
    @DeleteMapping("/DeleteFonctionalite/{idFonc}")
    public void DeleteEtt(@PathVariable String idFonc){
        IFoncService.deleteFonc(idFonc);
    }
    @PutMapping("/removeModel/{idModel}/{idFonc}")
    public void removeModel(@PathVariable String idModel,@PathVariable String idFonc){
        IFoncService.removeModel(idModel,idFonc);
    }
    @GetMapping("/bymenu/{nomMenu}")
    public List<FoncResponseDTO> getFonctionsByNomMenu(@PathVariable String nomMenu) {
        return IFoncService.getFonctionsByNomMenu(nomMenu);
    }
}
