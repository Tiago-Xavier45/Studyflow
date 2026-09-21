package com.tiagoxavier.study_service.controller;

import com.tiagoxavier.study_service.dto.DisciplineResponse;
import com.tiagoxavier.study_service.exception.ResourceNotFoundException;
import com.tiagoxavier.study_service.service.DisciplineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DisciplineController.class)
class DisciplineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DisciplineService disciplineService;

    @Test
    void CriarDisciplina() throws Exception {

        DisciplineResponse response = new DisciplineResponse(1L, "Java", "Estudar Java", 1L, null);

        when(disciplineService.createDiscipline(any())).thenReturn(response);

        String json = """
            {
                "name": "Java",
                "description": "Estudar Java",
                "userId": 1
            }
            """;

        mockMvc.perform(post("/disciplines").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("Java")).andExpect(jsonPath("$.description").value("Estudar Java")).andExpect(jsonPath("$.userId").value(1));

        verify(disciplineService).createDiscipline(any());
    }

    @Test
    void BuscarTodasAsDisciplinas() throws Exception {

        DisciplineResponse discipline1 = new DisciplineResponse(1L, "Java", "Estudar Java", 1L, null);

        DisciplineResponse discipline2 = new DisciplineResponse(2L, "PostgreSQL", "Estudar banco de dados", 1L, null);

        when(disciplineService.getAllDisciplines()).thenReturn(List.of(discipline1, discipline2));

        mockMvc.perform(get("/disciplines")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].name").value("Java")).andExpect(jsonPath("$[1].name").value("PostgreSQL"));

        verify(disciplineService).getAllDisciplines();
    }

    @Test
    void BuscarDisciplinaPorId() throws Exception {

        DisciplineResponse response = new DisciplineResponse(1L, "Java", "Estudar Java", 1L, null);

        when(disciplineService.getDisciplineById(1L)).thenReturn(response);

        mockMvc.perform(get("/disciplines/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("Java")).andExpect(jsonPath("$.description").value("Estudar Java")).andExpect(jsonPath("$.userId").value(1));

        verify(disciplineService).getDisciplineById(1L);
    }

    @Test
    void deveRetornar404QuandoDisciplinaNaoExistir() throws Exception {

        when(disciplineService.getDisciplineById(999L)).thenThrow(new ResourceNotFoundException("Disciplina não encontrada"));

        mockMvc.perform(get("/disciplines/999")).andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404)).andExpect(jsonPath("$.message").value("Disciplina não encontrada"));

        verify(disciplineService).getDisciplineById(999L);
    }

    @Test
    void AtualizarDisciplina() throws Exception {

        DisciplineResponse response = new DisciplineResponse(1L, "Java Avançado", "Spring Boot e Microsserviços", 1L, null);

        when(disciplineService.updateDiscipline(org.mockito.ArgumentMatchers.eq(1L), any())).thenReturn(response);

        String json = """
            {
                "name": "Java Avançado",
                "description": "Spring Boot e Microsserviços",
                "userId": 1
            }
            """;

        mockMvc.perform(put("/disciplines/1").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Java Avançado")).andExpect(jsonPath("$.description").value("Spring Boot e Microsserviços")).andExpect(jsonPath("$.userId").value(1));

        verify(disciplineService).updateDiscipline(org.mockito.ArgumentMatchers.eq(1L), any());
    }

    @Test
    void Retornar404AoAtualizarDisciplinaInexistente() throws Exception {

        when(disciplineService.updateDiscipline(org.mockito.ArgumentMatchers.eq(999L), any())).thenThrow(new ResourceNotFoundException("Disciplina não encontrada"));

        String json = """
            {
                "name": "Java",
                "description": "Estudar Java",
                "userId": 1
            }
            """;

        mockMvc.perform(put("/disciplines/999").contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404)).andExpect(jsonPath("$.message").value("Disciplina não encontrada"));

        verify(disciplineService).updateDiscipline(org.mockito.ArgumentMatchers.eq(999L), any());
    }

    @Test
    void deveExcluirDisciplina() throws Exception {

        mockMvc.perform(delete("/disciplines/1")).andExpect(status().isNoContent());

        verify(disciplineService).deleteDiscipline(1L);
    }

    @Test
    void deveRetornar404AoExcluirDisciplinaInexistente() throws Exception {

        doThrow(new ResourceNotFoundException("Disciplina não encontrada")).when(disciplineService).deleteDiscipline(999L);

        mockMvc.perform(delete("/disciplines/999")).andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404)).andExpect(jsonPath("$.message").value("Disciplina não encontrada"));

        verify(disciplineService).deleteDiscipline(999L);
    }

}