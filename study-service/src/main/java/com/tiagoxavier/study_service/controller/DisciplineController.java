package com.tiagoxavier.study_service.controller;

import com.tiagoxavier.study_service.dto.DisciplineRequest;
import com.tiagoxavier.study_service.dto.DisciplineResponse;
import com.tiagoxavier.study_service.service.DisciplineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disciplines")
public class DisciplineController {

    private final DisciplineService disciplineService;

    public DisciplineController(DisciplineService disciplineService) {
        this.disciplineService = disciplineService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DisciplineResponse createDiscipline(@Valid @RequestBody DisciplineRequest request) {
        return disciplineService.createDiscipline(request);
    }

    @GetMapping
    public List<DisciplineResponse> getAllDisciplines() {
        return disciplineService.getAllDisciplines();
    }

    @GetMapping("/{id}")
    public DisciplineResponse getDisciplineById(@PathVariable Long id) {
        return disciplineService.getDisciplineById(id);
    }

    @PutMapping("/{id}")
    public DisciplineResponse updateDiscipline(@PathVariable Long id, @Valid @RequestBody DisciplineRequest request) {

        return disciplineService.updateDiscipline(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDiscipline(@PathVariable Long id) {
        disciplineService.deleteDiscipline(id);
    }
}