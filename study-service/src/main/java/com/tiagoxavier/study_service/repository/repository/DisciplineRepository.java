package com.tiagoxavier.study_service.repository;

import com.tiagoxavier.study_service.entity.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
}