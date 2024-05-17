package roomescape.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @DisplayName("이메일을 생성한다")
    @Test
    void when_createEmail_then_created() {
        // when, then
        assertThatCode(() -> new Email("pkpkpkpk@woowa.net"))
                .doesNotThrowAnyException();
    }

    @DisplayName("잘못된 이메일 형식으로 생성하면 예외가 발생한다")
    @ValueSource(strings = {"", "pkpk", "pkpk@woowa", "pkpk@woowa.", "pkpk@woowa.1"})
    @ParameterizedTest
    void when_createEmailWithInvalidFormat_then_throwException(String email) {
        // when, then
        assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
