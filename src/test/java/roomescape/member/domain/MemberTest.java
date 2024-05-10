package roomescape.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.handler.exception.CustomException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @DisplayName("실패: 이름은 1자 이상 10자 이하여야 한다")
    @ParameterizedTest
    @ValueSource(strings = {"", "01234567890"})
    void validateNameFailTest(String name) {
        assertThatThrownBy(() -> {
            new Member(1L, name, "abcd@gmail.com", "2580");
        }).isInstanceOf(CustomException.class);
    }

    @DisplayName("성공: 이름은 1자 이상 10자 이하여야 한다")
    @ParameterizedTest
    @ValueSource(strings = {"0", "0123456789"})
    void validateNameSuccessTest(String name) {
        assertThatCode(() -> {
            new Member(1L, name, "abcd@gmail.com", "2580");
        }).doesNotThrowAnyException();
    }

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
