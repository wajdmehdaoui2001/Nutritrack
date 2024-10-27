package com.nutritTrack.project.repositories;

import com.nutritTrack.project.entities.Consultation;
import com.nutritTrack.project.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultationRepository extends JpaRepository<Consultation,Long> {
}
