package com.nutritTrack.project.services;

import com.nutritTrack.project.entities.Question;
import com.nutritTrack.project.repositories.QuestionRepository;
import com.nutritTrack.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl  implements QuestionService{

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Question createQuestion(Question question, User user) {

        userRepository.findById(user.getId());
        Question createdQuestion = new Question();
        createdQuestion.setQuestion(question.getQuestion());
        createdQuestion.setUser(question.getUser());
        createdQuestion.setId(question.getId());
        return questionRepository.save(createdQuestion);
    }

    @Override
    public Question findQuestionById(Long id) throws Exception {
        Optional<Question> opt = questionRepository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new Exception("question not found with id"+id);
    }

    @Override
    public void deleteQuestion(Long id) throws Exception {
            findQuestionById(id);
            questionRepository.deleteById(id);
    }


    @Override
    public List<Question> findAllQuestion() {
        return questionRepository.findAll();
    }
}
