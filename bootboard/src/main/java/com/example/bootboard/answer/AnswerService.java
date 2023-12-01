package com.example.bootboard.answer;

import com.example.bootboard.question.DataNotFoundException;
import com.example.bootboard.question.Question;
import com.example.bootboard.question.QuestionRepository;
import com.example.bootboard.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public void create (Question question, String content, SiteUser author){
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
    }

    public Answer getAnswer(Integer id){
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()){
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void update(Answer answer, String content){
        answer.setContent(content);
        answer.setUpdatedDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

}
