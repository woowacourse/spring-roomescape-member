package roomescape.member.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LoginRequestTest {

    @Test
    @DisplayName("유효한 이메일과 비밀번호로 LoginRequest를 생성할 수 있다")
    void test1() {
        // given
        String email = "mimi@email.com";
        String password = "password";

        // when
        LoginRequest request = new LoginRequest(email, password);

        // then
        assertThat(request.email()).isEqualTo(email);
        assertThat(request.password()).isEqualTo(password);
    }

    @Test
    @DisplayName("이메일이 null이면 예외가 발생한다")
    void test2() {
        // given
        String email = null;

        // when & then
        assertThatThrownBy(() -> new LoginRequest(email, "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이메일을 입력해주세요.");
    }

    @Test
    @DisplayName("이메일이 빈 값이면 예외가 발생한다")
    void test3() {
        // given
        String email = "  ";

        // when & then
        assertThatThrownBy(() -> new LoginRequest(email, "password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이메일을 입력해주세요.");
    }

    @Test
    @DisplayName("비밀번호가 null이면 예외가 발생한다")
    void test4() {
        // given
        String password = null;

        // when & then
        assertThatThrownBy(() -> new LoginRequest("mimi@email.com", password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비밀번호를 입력해주세요.");
    }

    @Test
    @DisplayName("비밀번호가 빈 값이면 예외가 발생한다")
    void test5() {
        // given
        String password = " ";

        // when & then
        assertThatThrownBy(() -> new LoginRequest("mimi@email.com", password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비밀번호를 입력해주세요.");
    }
}
