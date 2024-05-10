package roomescape.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.handler.exception.CustomException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @DisplayName("실패: 이메일 형식에 맞지 않은 멤버 생성")
    @Test
    void invalidEmailTest() {
        assertThatThrownBy(() -> new Member(1L, "name", "asd%google.com", "2580"))
                .isInstanceOf(CustomException.class);
    }

    @DisplayName("성공: 이메일 형식에 맞지 않은 멤버 생성")
    @Test
    void validEmailTest() {
        assertThatCode(() -> new Member(1L, "name", "asd@google.com", "2580"))
                .doesNotThrowAnyException();
    }
}
