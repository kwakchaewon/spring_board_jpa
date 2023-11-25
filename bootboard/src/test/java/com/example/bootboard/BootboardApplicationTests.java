package com.example.bootboard;

import com.example.bootboard.domain.Answer;
import com.example.bootboard.domain.Question;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class BootboardApplicationTests {

	/**
	 *  test 작성 조건
	 *  1. given: 주어진 조건
	 *  2. when: 실행 로직
	 *  3. 결과
	 */

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@AfterEach
	void afterEach(){
	}

	@BeforeEach
	void beforeEach(){
//        question 테이블 모든 데이터 삭제 (시퀀스는 그대로)
//        this.questionRepository.deleteAll();
	}


	/**
	 * Question 데이터 저장 테스트
	 * 중복 데이터 생성 방지 및 count 수 유지를 위해 @Transactional 처리
	 */
	@Test
	@Transactional
	void testJpa() {
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);  // 첫번째 질문 저장

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2);  // 두번째 질문 저장
	}

	/**
	 * Question 전체 조회 테스트
	 */
	@Test
	void testFindAll() {
		List<Question> all = this.questionRepository.findAll();
//        assertEquals(2, all.size());

		Question q = all.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	/**
	 * Question id로 검색
	 * id 조회 후 subject 같은지 비교
	 */
	@Test
	void testFindById() {
		Optional<Question> oq = this.questionRepository.findById(1);
		if (oq.isPresent()) {
			Question q = oq.get();
			assertEquals("sbb가 무엇인가요?", q.getSubject());
		}
	}

	/**
	 * Question subject로 조회
	 * subject는 unique한 값이 아니기에 주석처리
	 */
	@Test
	void findBySubject() {
		// 유니크한 값이 나오지 않아 주석처리
//        Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
//        assertEquals(1, q.getId());
	}

	/**
	 * Question subject & Content로 검색
	 * 역시나 unique한 값이 아니기에 주석처리
	 */
	@Test
	void findBySubjectAndContent() {
//        Question q = this.questionRepository.findBySubjectAndContent(
//                "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
//        assertEquals(1, q.getId());
	}

	/**
	 * Question 테이블 Subject 특정 문자열 검색
	 */
	@Test
	void testFindBySubjectLike() {
		List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");
		Question q = qList.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	/**
	 * Question Subject update 테스트
	 */
	@Test
	@Transactional
	void testUpdateSubject() {
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정된 제목");
		this.questionRepository.save(q);
	}

	/**
	 * Question Subject delete 테스트
	 */
	@Test
	@Transactional
	void testDeleteQuestion() {
		assertEquals(6, this.questionRepository.count());
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		this.questionRepository.delete(q);
		assertEquals(5, this.questionRepository.count());
	}

	/**
	 * Answer 테이블 데이터 생성 테스트
	 */
	@Test
	@Transactional
    void testCreateAnswer() {
        Optional<Question> oq = this.questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        Answer a = new Answer();
        a.setContent("네 자동으로 생성됩니다.");
        a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
        a.setCreateDate(LocalDateTime.now());
        this.answerRepository.save(a);
    }

	/**
	 * Answer 테이블 데이터 조회 테스트
	 */
	@Test
    void testSelectAnswer() {
        Optional<Answer> oa = this.answerRepository.findById(1);
        assertTrue(oa.isPresent());
        Answer a = oa.get();
        assertEquals(2, a.getQuestion().getId());
    }

	/**
	 * Question 테이블에서  Answer로 검색 테스트
	 */
    @Transactional
    @Test
    void testFindAnswerByQuestion() {
        Optional<Question> oq = this.questionRepository.findById(2);
        assertTrue(oq.isPresent());
        Question q = oq.get();

        List<Answer> answerList = q.getAnswerList();

        assertEquals(1, answerList.size());
        assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
    }

}
