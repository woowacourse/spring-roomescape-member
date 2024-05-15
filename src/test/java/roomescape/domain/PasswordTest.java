package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {

    @Test
    @DisplayName("생성 테스트")
    void create() {
        assertThatCode(() -> new Password("values"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("비밀번호는 6글자 이상이다.")
    void validateMinLength() {
        String shortPassword = "*".repeat(5);

        assertThatThrownBy(() -> new Password(shortPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호의 길이는 6 ~ 12글자 사이입니다.");
    }

    @Test
    @DisplayName("비밀번호는 12글자 이하이다.")
    void validateMaxLength() {
        String longPassword = "*".repeat(13);

        assertThatThrownBy(() -> new Password(longPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호의 길이는 6 ~ 12글자 사이입니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "\n", "\r"})
    @DisplayName("비밀번호에는 공백이 포함되면 안된다.")
    void validateIncludeWhiteSpace(String source) {
        String includeWhiteSpacePassword = "*".repeat(3) + source + "*".repeat(3);

        assertThatThrownBy(() -> new Password(includeWhiteSpacePassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호는 공백을 포함할 수 없습니다.");
    }

    @Test
    @DisplayName("비밀번호는 *로 마스킹 처리된다.")
    void mask() {
        Password password = new Password("a".repeat(6));

        assertThat(password.value()).isEqualTo("******");
    }
}
