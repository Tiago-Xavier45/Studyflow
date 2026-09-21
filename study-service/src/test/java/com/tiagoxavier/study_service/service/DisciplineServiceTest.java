package com.tiagoxavier.study_service.service;

import com.tiagoxavier.study_service.dto.DisciplineRequest;
import com.tiagoxavier.study_service.dto.DisciplineResponse;
import com.tiagoxavier.study_service.entity.Discipline;
import com.tiagoxavier.study_service.exception.ResourceNotFoundException;
import com.tiagoxavier.study_service.repository.DisciplineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DisciplineServiceTest {

    @Mock
    private DisciplineRepository disciplineRepository;

    @InjectMocks
    private DisciplineService disciplineService;

    @Test
    void CriarDisciplina() {

        DisciplineRequest request = new DisciplineRequest();
        request.setName("Java");
        request.setDescription("Estudar Java");
        request.setUserId(1L);

        Discipline discipline = new Discipline();
        discipline.setName("Java");
        discipline.setDescription("Estudar Java");
        discipline.setUserId(1L);

        when(disciplineRepository.save(any(Discipline.class))).thenReturn(discipline);DisciplineResponse response = disciplineService.createDiscipline(request);

        assertEquals("Java", response.getName());
        assertEquals("Estudar Java", response.getDescription());
        assertEquals(1L, response.getUserId());

        verify(disciplineRepository).save(any(Discipline.class));
    }

    @Test
    void BuscarTodasAsDisciplinas() {

        Discipline discipline1 = new Discipline();
        discipline1.setName("Java");
        discipline1.setDescription("Estudar Java");
        discipline1.setUserId(1L);

        Discipline discipline2 = new Discipline();
        discipline2.setName("Banco de Dados");
        discipline2.setDescription("Estudar PostgreSQL");
        discipline2.setUserId(1L);

        when(disciplineRepository.findAll()).thenReturn(List.of(discipline1, discipline2));List<DisciplineResponse> response = disciplineService.getAllDisciplines();

        assertEquals(2, response.size());
        assertEquals("Java", response.get(0).getName());
        assertEquals("Banco de Dados", response.get(1).getName());

        verify(disciplineRepository).findAll();
    }

    @Test
    void BuscarDisciplinaPorId() {

        Discipline discipline = new Discipline();
        discipline.setName("Java");
        discipline.setDescription("Estudar Java");
        discipline.setUserId(1L);

        when(disciplineRepository.findById(1L)).thenReturn(Optional.of(discipline));DisciplineResponse response = disciplineService.getDisciplineById(1L);

        assertEquals("Java", response.getName());
        assertEquals("Estudar Java", response.getDescription());
        assertEquals(1L, response.getUserId());

        verify(disciplineRepository).findById(1L);
    }

    @Test
    void LancarExcecaoQuandoDisciplinaNaoExistir() {

        when(disciplineRepository.findById(999L)).thenReturn(Optional.empty());assertThrows(ResourceNotFoundException.class, () -> disciplineService.getDisciplineById(999L));

        verify(disciplineRepository).findById(999L);
    }

    @Test
    void AtualizarDisciplina() {

        DisciplineRequest request = new DisciplineRequest();
        request.setName("Java Avançado");
        request.setDescription("Spring Boot e Microsserviços");
        request.setUserId(1L);

        Discipline discipline = new Discipline();
        discipline.setName("Java");
        discipline.setDescription("Java básico");
        discipline.setUserId(1L);

        when(disciplineRepository.findById(1L)).thenReturn(Optional.of(discipline));when(disciplineRepository.save(any(Discipline.class))).thenReturn(discipline);

        DisciplineResponse response = disciplineService.updateDiscipline(1L, request);

        assertEquals("Java Avançado", response.getName());
        assertEquals("Spring Boot e Microsserviços", response.getDescription());
        assertEquals(1L, response.getUserId());

        verify(disciplineRepository).findById(1L);
        verify(disciplineRepository).save(discipline);
    }

    @Test
    void LancarExcecaoAoAtualizarDisciplinaInexistente() {

        DisciplineRequest request = new DisciplineRequest();
        request.setName("Java");
        request.setDescription("Estudar Java");
        request.setUserId(1L);

        when(disciplineRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> disciplineService.updateDiscipline(999L, request));

        verify(disciplineRepository).findById(999L);
    }

    @Test
    void ExcluirDisciplina() {

        Discipline discipline = new Discipline();
        discipline.setName("Java");
        discipline.setUserId(1L);

        when(disciplineRepository.findById(1L)).thenReturn(Optional.of(discipline));

        disciplineService.deleteDiscipline(1L);

        verify(disciplineRepository).findById(1L);
        verify(disciplineRepository).delete(discipline);
    }

    @Test
    void LancarExcecaoAoExcluirDisciplinaInexistente() {

        when(disciplineRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> disciplineService.deleteDiscipline(999L));

        verify(disciplineRepository).findById(999L);
    }
}