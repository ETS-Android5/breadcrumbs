package com.spe.breadcrumbs.web.controller;

import com.spe.breadcrumbs.dao.*;
import com.spe.breadcrumbs.entity.Choice;
import com.spe.breadcrumbs.entity.Question;
import com.spe.breadcrumbs.entity.Quiz;
import com.spe.breadcrumbs.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/participants")


public class UserController {
    private UserDAO userDAO = new UserDbDAO();
    private QuestionDAO questionDAO = new QuestionDbDAO();
    private QuizDAO quizDAO = new QuizDbDAO();
    private AttemptDAO attemptDAO = new AttemptDbDAO();

    @RequestMapping(method = RequestMethod.GET)
    public String participants(Model m){
        m.addAttribute("users",userDAO.getAllUsers());
        return "views/participants";
    }

    @RequestMapping(method = RequestMethod.GET,value = "{id}")
    public String getUserDetail(@PathVariable Long id,Model m){
        User match = userDAO.getUserWithQuiz(id);
        List<Question> questions = match.getQuiz().getQuestions();
        for(Question q: questions){
            //get no of attempts plus score
            int noOfAttempts = attemptDAO.getAttempts(q.getId(),id).size();
            int score;
            if (noOfAttempts == 1) score = 100;
            else if (noOfAttempts == 2) score = 50;
            else if (noOfAttempts == 3) score = 25;
            else score = 0;
            q.setNoOfAttempts(noOfAttempts);
            q.setScore(score);
        }
        m.addAttribute("user",match);

        return "views/participants_userProfile";
    }

    @RequestMapping(method = RequestMethod.GET,value = "{userId}/questions/{questionId}")
    public String getQuestionDetail(@PathVariable Long userId,@PathVariable Long questionId,Model m){
        User u = userDAO.getUserWithQuiz(userId);
        Quiz quiz = u.getQuiz();
        Question q = quiz.findQuestion(questionId);
        List<Choice> choices = questionDAO.getChoices(questionId);
        q.setChoices(choices);
        List<Choice> attempts = attemptDAO.getAttempts(questionId,userId);
        q.setAttempts(attempts);
        m.addAttribute("question",q);
        return "views/participants_userProfile_question";
    }

}
