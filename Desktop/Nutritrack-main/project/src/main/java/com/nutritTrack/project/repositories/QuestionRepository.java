package com.nutritTrack.project.repositories;

import com.nutritTrack.project.entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,Long> {
}
