package com.example.bootboard.question;

import com.example.bootboard.answer.AnswerForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/question")
@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/list")
    public String list(Model model) {
        List<Question> questionList = this.questionService.getList();
        model.addAttribute("questionList", questionList);
        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm){
        Question question = questionService.getQuestion(id);
        model.addAttribute("question",question);
        return "question_detail";
    }

    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm){
        return "question_form";
    }

    /**
     * 질문등록 컨트롤러
     */
//    @PostMapping("/create")
//    public String questionCreate(@RequestParam String subject, @RequestParam String content){
//        return "redirect:/question/list";
//    }


    /**
     * 폼 & validation 적용 질문 등록 컨트롤러
     * 매개변수 (subject, content) -> QuestionForm:
     */
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult){
        
        // 1. 에러가 있을 경우 다시 폼 작성
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        //2. 에러가 없다면 질문 등록
        this.questionService.create(questionForm.getSubject(), questionForm.getContent());
        return "redirect:/question/list";
    }






}
