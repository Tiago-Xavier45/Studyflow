package com.tiagoxavier.study_service.service;

import com.tiagoxavier.study_service.dto.DisciplineRequest;
import com.tiagoxavier.study_service.dto.DisciplineResponse;
import com.tiagoxavier.study_service.exception.ResourceNotFoundException;
import com.tiagoxavier.study_service.entity.Discipline;
import com.tiagoxavier.study_service.repository.DisciplineRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DisciplineService {

    private final DisciplineRepository disciplineRepository;

    public DisciplineService(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    public DisciplineResponse createDiscipline(DisciplineRequest request) {

        Discipline discipline = new Discipline();

        discipline.setName(request.getName());
        discipline.setDescription(request.getDescription());
        discipline.setUserId(request.getUserId());
        discipline.setCreatedAt(LocalDateTime.now());

        Discipline savedDiscipline = disciplineRepository.save(discipline);

        return new DisciplineResponse(savedDiscipline.getId(), savedDiscipline.getName(), savedDiscipline.getDescription(), savedDiscipline.getUserId(), savedDiscipline.getCreatedAt());
    }

    public List<DisciplineResponse> getAllDisciplines() {

        return disciplineRepository.findAll().stream().map(discipline -> new DisciplineResponse(discipline.getId(), discipline.getName(), discipline.getDescription(), discipline.getUserId(), discipline.getCreatedAt())).toList();
    }

    public DisciplineResponse getDisciplineById(Long id) {

        Discipline discipline = disciplineRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada"));

        return new DisciplineResponse(discipline.getId(), discipline.getName(), discipline.getDescription(), discipline.getUserId(), discipline.getCreatedAt());
    }

    public DisciplineResponse updateDiscipline(Long id, DisciplineRequest request) {

        Discipline discipline = disciplineRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada"));

        discipline.setName(request.getName());
        discipline.setDescription(request.getDescription());
        discipline.setUserId(request.getUserId());

        Discipline updatedDiscipline = disciplineRepository.save(discipline);

        return new DisciplineResponse(updatedDiscipline.getId(), updatedDiscipline.getName(), updatedDiscipline.getDescription(), updatedDiscipline.getUserId(), updatedDiscipline.getCreatedAt());
    }

    public void deleteDiscipline(Long id) {

        Discipline discipline = disciplineRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Disciplina não encontrada"));

        disciplineRepository.delete(discipline);
    }
}