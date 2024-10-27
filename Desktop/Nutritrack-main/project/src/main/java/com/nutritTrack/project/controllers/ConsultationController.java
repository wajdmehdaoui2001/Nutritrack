package com.nutritTrack.project.controllers;

import com.nutritTrack.project.entities.Consultation;
import com.nutritTrack.project.entities.Recipe;
import com.nutritTrack.project.services.ConsultationService;
import com.nutritTrack.project.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consultations")
public class ConsultationController {
    @Autowired
    private ConsultationService consultationService;
    /*@Autowired
    private UserService userService;*/

    /*@PostMapping()
    public Consultation createConsultation(@RequestBody Consultation consultation ,@RequestHeader("Authorization") String jwt) throws Exception {

        User user=userService.findUserByJwt(jwt);
        Consultation createdConsultation=consultationService.createConsultation(consultation, user);
        return createdConsultation;

    } */

    @PutMapping("/{id}")
    public Consultation updateConsultation(@RequestBody Consultation consultation , @PathVariable Long id) throws Exception {


        Consultation updatedConsultation=consultationService.updateConsultation(consultation, id);
        return updatedConsultation;

    }
    @GetMapping()
    public List<Consultation> getAllConsultation() throws Exception{
        List<Consultation> consultations=consultationService.findAllConsultation();
        return consultations;
    }
    @DeleteMapping("/{consultationId}")
    public ResponseEntity<Map<String, String>> deleteConsultation(@PathVariable Long consultationId) throws Exception {
        consultationService.deleteConsultation(consultationId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Consultation deleted successfully");
        return ResponseEntity.ok(response);
    }
}
