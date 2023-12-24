package com.administration.controller;

import com.administration.dto.ProfilRequestDTO;
import com.administration.dto.ProfilResponseDTO;
import com.administration.dto.ProfilUpdateDTO;
import com.administration.service.IProfilService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="http://localhost:4200")

public class ProfileController {
    
     IProfilService IProfilService;

    public ProfileController(IProfilService IProfilService) {
        this.IProfilService = IProfilService;
    }

    @GetMapping(path="/profiles")
    public List<ProfilResponseDTO> allProfiles()
    {

        return IProfilService.listProfiles();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(path="/ajouteprofile")
    public ProfilResponseDTO save(@RequestBody ProfilRequestDTO profilRequestDTO){
        return IProfilService.addProfile(profilRequestDTO);
    }
    @GetMapping(path = "/profile/{idProfile}")
    public ProfilResponseDTO getProfile(@PathVariable String idProfile){

        return IProfilService.getProfile(idProfile);
    }



    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update-profile/")
    @ResponseBody
    public void UpdateProfileDTO(@RequestBody ProfilUpdateDTO dto) {
        IProfilService.updateProfileDTO(dto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/affecterFonctionaliteToProfile/{idFonc}/{idProfile}")
    public void affecterFonctionaliteToProfile(@PathVariable String idFonc,@PathVariable String idProfile){
        IProfilService.affecterFoncToProfile(idFonc,idProfile);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/removeFonc/{idFonc}/{idProfile}")
    public void removeFonc(@PathVariable String idFonc,@PathVariable String idProfile){
        IProfilService.removeFonc(idFonc,idProfile);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/affecterModelToProfile/{idModel}/{idProfile}")
    public void affecterModelToProfile(@PathVariable String idModel,@PathVariable String idProfile){
        IProfilService.affecterModelToProfile(idModel,idProfile);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/removeModelToProfile/{idProfile}")
    public void removeModelToProfile(@PathVariable String idProfile){
        IProfilService.removeModel(idProfile);
    }
    @DeleteMapping("/deleteProfile/{idProfile}")
    public void deleteProfile(@PathVariable String idProfile){
        IProfilService.deleteProfile(idProfile);
    }

    @GetMapping(path="/searchPageProfils")
    public List<ProfilResponseDTO> searchPageUsers(
            @RequestParam(name = "Keyword",defaultValue = "")String kw,
            @RequestParam(name = "desc",defaultValue = "")String desc,
            @RequestParam (name = "page",defaultValue = "0")int page
            ,@RequestParam(name = "size",defaultValue = "10")int size)
    {

        return IProfilService.findProfilsBynames("%"+kw+"%","%"+desc+"%",page,size);
    }

}
