package com.nutritTrack.project.services;

import com.nutritTrack.project.entities.Question;

import java.util.List;

public interface QuestionService {
    public Question createQuestion(Question question, User user);
    public Question findQuestionById(Long id) throws Exception ;
    public void deleteQuestion(Long id) throws Exception ;

    public List<Question> findAllQuestion();
}
