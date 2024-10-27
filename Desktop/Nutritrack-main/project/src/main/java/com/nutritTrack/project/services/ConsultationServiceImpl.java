package com.nutritTrack.project.services;

import com.nutritTrack.project.entities.Consultation;
import com.nutritTrack.project.entities.Recipe;
import com.nutritTrack.project.repositories.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsultationServiceImpl  implements ConsultationService{
    @Autowired
    private ConsultationRepository consultationRepository;
    @Override
    public Consultation findConsultationById(Long id) throws Exception {
        Optional<Consultation> opt = consultationRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new Exception("Consultation not found"+id);
    }

    @Override
    public void deleteConsultation(Long id) throws Exception {
        findConsultationById(id);
        consultationRepository.deleteById(id);

    }

    @Override
    public Consultation updateConsultation(Consultation consultation, Long Id) throws Exception {
        Consultation oldConsultation=findConsultationById(Id);
        if(oldConsultation.getCommentaires() !=null){
            oldConsultation.setCommentaires(oldConsultation.getCommentaires());

        }
        if(oldConsultation.getDate() !=null){
            oldConsultation.setDate(oldConsultation.getDate());

        }
        if(oldConsultation.getLocalistaion() !=null){
            oldConsultation.setLocalistaion(oldConsultation.getLocalistaion());

        }

        return consultationRepository.save(oldConsultation);
    }

    @Override
    public List<Consultation> findAllConsultation() {
        return consultationRepository.findAll();
    }
}
