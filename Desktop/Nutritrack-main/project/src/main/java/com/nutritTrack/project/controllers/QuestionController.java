package com.nutritTrack.project.controllers;
import com.nutritTrack.project.entities.Question;
import com.nutritTrack.project.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @PostMapping()
    public Question createQuestion(@RequestBody Question question, User user) throws Exception {
        Question createdQuestion=questionService.createQuestion(question,user);
        return createdQuestion;

    }
    @DeleteMapping("/{questionId}")
    public ResponseEntity<Map<String, String>> deleteQuestion(@PathVariable Long questionId) throws Exception {
        questionService.deleteQuestion(questionId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Question deleted successfully");
        return ResponseEntity.ok(response);
    }
    @GetMapping()
    public List<Question> getAllQuestion() throws Exception{
        List<Question> questions=questionService.findAllQuestion();
        return questions;
    }

}
