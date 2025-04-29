package com.aryan.eQuiz.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.aryan.eQuiz.model.Question;
import com.aryan.eQuiz.model.QuestionForm;
import com.aryan.eQuiz.repository.QuestionRepo;
import com.aryan.eQuiz.repository.ResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.aryan.eQuiz.model.Result;

@Service
public class QuizService {

    @Autowired
    Question question;
    @Autowired
    QuestionForm qForm;
    @Autowired
    QuestionRepo qRepo;
    @Autowired
    Result result;
    @Autowired
    ResultRepo rRepo;

    public QuestionForm getQuestions() {
        List<Question> allQues = qRepo.findAll();

        // Print all questions in the database for debugging
        System.out.println("Questions in database: ");
        allQues.forEach(q -> System.out.println(q));

        // Check if questions are available
        if (allQues.isEmpty()) {
            throw new RuntimeException("No questions available in the database.");
        }

        List<Question> qList = new ArrayList<Question>();
        Random random = new Random();

        // Ensure that we only fetch as many questions as available in the database
        int numOfQuestions = Math.min(allQues.size(), 10);

        // List to track already selected questions
        List<Question> selectedQuestions = new ArrayList<>();

        // Select questions randomly without repetition
        while (qList.size() < numOfQuestions) {
            int randomIndex = random.nextInt(allQues.size());
            Question selectedQuestion = allQues.get(randomIndex);

            // Ensure the selected question is not repeated in the current session
            if (!selectedQuestions.contains(selectedQuestion)) {
                qList.add(selectedQuestion);
                selectedQuestions.add(selectedQuestion); // Mark it as selected for this quiz session
            }
        }

        qForm.setQuestions(qList);

        // Print the selected questions for debugging
        System.out.println("Selected questions for the quiz: ");
        qList.forEach(q -> System.out.println(q));

        return qForm;
    }

    public int getResult(QuestionForm qForm) {
        int correct = 0;

        // Check the answers
        for (Question q : qForm.getQuestions()) {
            if (q.getAns() == q.getChose()) {
                correct++;
            }
        }

        return correct;
    }

    public void saveScore(Result result) {
        Result saveResult = new Result();
        saveResult.setUsername(result.getUsername());
        saveResult.setTotalCorrect(result.getTotalCorrect());
        rRepo.save(saveResult);
    }

    public List<Result> getTopScore() {
        List<Result> sList = rRepo.findAll(Sort.by(Sort.Direction.DESC, "totalCorrect"));
        return sList;
    }
}
