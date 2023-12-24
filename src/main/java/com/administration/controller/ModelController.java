package com.administration.controller;

import com.administration.dto.ModelRequestDTO;
import com.administration.dto.ModelResponseDTO;
import com.administration.dto.ModelUpdateDTO;
import com.administration.service.IModelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ModelController {
    IModelService IModelService;

    public ModelController(IModelService IModelService) {
        this.IModelService = IModelService;
    }
    @GetMapping(path="/models")
    public List<ModelResponseDTO> allModels()
    {

        return IModelService.listModels();
    }

    @PostMapping(path="/ajoutemodel")
    public ModelResponseDTO save(@RequestBody ModelRequestDTO modelRequestDTO){
        return IModelService.addModel(modelRequestDTO);
    }
    @GetMapping(path = "/model/{idModel}")
    public ModelResponseDTO getModel(@PathVariable String idModel){

        return IModelService.getModel(idModel);
    }

    @PutMapping("/update-model/")
    @ResponseBody
    public void UpdateModelDTO(@RequestBody ModelUpdateDTO dto) {
        IModelService.updateModelDTO(dto);
    }

    @DeleteMapping("/deleteModel/{idModel}")
    public void deleteModel(@PathVariable String idModel){
        IModelService.deleteModel(idModel);
    }

}
