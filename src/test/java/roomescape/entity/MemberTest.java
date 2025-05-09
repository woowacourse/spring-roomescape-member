package roomescape.entity;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    @DisplayName("유효한 값을 입력할 경우, 성공적으로 Member를 생성한다.")
    @Test
    void buildMember() {
        // given
        Long id = 1L;
        String name = "테스트";
        String email = "test@example.com";
        String password = "password123";

        // when & then
        assertThatCode(() -> new Member(id, name, email, password)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    @DisplayName("이름이 null이나 공백을 입력할 경우, 예외가 발생한다.")
    void validationName(String name) {
        // given
        Long id = 1L;
        String email = "test@example.com";
        String password = "password123";

        // when & then
        assertThatThrownBy(() -> new Member(id, name, email, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름에 공백을 입력할 수 없습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    @DisplayName("이메일이 null이나 공백을 입력할 경우, 예외가 발생한다.")
    void validationEmailNullOrEmpty(String email) {
        // given
        Long id = 1L;
        String name = "테스트";
        String password = "password123";

        // when & then
        assertThatThrownBy(() -> new Member(id, name, email, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이메일에 공백을 입력할 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalidemail", "user@", "@example.com", "user@.com", "user@domain", "user.com"})
    @DisplayName("이메일 형식이 올바르지 않을 경우, 예외가 발생한다.")
    void validationEmailFormat(String email) {
        // given
        Long id = 1L;
        String name = "테스트";
        String password = "password123";

        // when & then
        assertThatThrownBy(() -> new Member(id, name, email, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 유효하지 않은 이메일 형식입니다.");
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    @DisplayName("비밀번호가 null이나 공백을 입력할 경우, 예외가 발생한다.")
    void validationPassword(String password) {
        // given
        Long id = 1L;
        String name = "테스트";
        String email = "test@example.com";

        // when & then
        assertThatThrownBy(() -> new Member(id, name, email, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비밀번호에 공백을 입력할 수 없습니다.");
    }
}
