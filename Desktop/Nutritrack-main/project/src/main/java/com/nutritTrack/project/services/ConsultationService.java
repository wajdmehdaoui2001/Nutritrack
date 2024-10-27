package com.nutritTrack.project.services;

import com.nutritTrack.project.entities.Consultation;
import com.nutritTrack.project.entities.Recipe;

import java.util.List;

public interface ConsultationService {

    public Consultation findConsultationById(Long id) throws Exception ;
    public void deleteConsultation(Long id) throws Exception ;
    public Consultation updateConsultation(Consultation consultation,Long Id) throws Exception ;
    //public Consultation  createConsultation(Consultation consultation, User user);

    public List<Consultation> findAllConsultation();
}
