package nextstep.qna.domain;

import nextstep.qna.CannotDeleteException;
import nextstep.users.domain.NsUserTest;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class QuestionTest {
    public static final Question Q1 = new Question(NsUserTest.JAVAJIGI, "title1", "contents1");
    public static final Question Q2 = new Question(NsUserTest.SANJIGI, "title2", "contents2");

    @Test
    void 질문자가_로그인유저인지() {
        assertThatThrownBy(() -> Q1.delete(NsUserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);

        assertThatThrownBy(() -> Q2.delete(NsUserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 답변글의_작성자가_질문자가_아닐_때() {
        Q1.addAnswer(AnswerTest.A2);
        assertThatThrownBy(() -> Q1.delete(NsUserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);

        Q2.addAnswer(AnswerTest.A1);
        assertThatThrownBy(() -> Q2.delete(NsUserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
    @Test
    void 여러_답변이_있을_때_타인의_답변이_있는_경우() {
        Q1.addAnswer(AnswerTest.A1);
        Q1.addAnswer(AnswerTest.A2);
        assertThatThrownBy(() -> Q1.delete(NsUserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 답변글의_작성자가_질문자인_경우() throws CannotDeleteException {
        Q1.delete(NsUserTest.JAVAJIGI);
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    void 모든_답변글의_작성자가_질문자인_경우() throws CannotDeleteException {
        Q1.addAnswer(AnswerTest.A1);
        Q1.addAnswer(AnswerTest.A3);
        Q1.delete(NsUserTest.JAVAJIGI);
        assertThat(Q1.isDeleted()).isTrue();
    }

    @Test
    void 질문을_삭제할_때는_모든_답변글도_삭제() throws CannotDeleteException {
        Q1.addAnswer(AnswerTest.A1);
        Q1.addAnswer(AnswerTest.A3);
        Q1.delete(NsUserTest.JAVAJIGI);
        assertThat(Q1.isDeleted()).isTrue();

        assertThat(Q1.getAnswers().stream()
                .map(Answer::isDeleted)
                .filter(b -> !b)
                .collect(Collectors.toList())
        ).isEmpty();
    }

    @Test
    void 질문_삭제_시_히스토리_생성() throws CannotDeleteException {
//        DeleteHistory history = Q1.getHistoryWithDeleting(NsUserTest.JAVAJIGI);
//        assertThat(Q1.isDeleted()).isTrue();
//        assertThat(history).isEqualTo(new DeleteHistory(ContentType.QUESTION, Q1.getId(), Q1.getWriter(), Q1.getUpdatedDate()));
    }
}

