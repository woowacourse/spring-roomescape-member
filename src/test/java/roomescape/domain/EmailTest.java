package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @DisplayName("이메일 형식이 잘못된 경우 예외가 발생한다.")
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"asdasd@@naver.com", "aa@", "@name.com", "aa@aa", " "})
    void emailFormatFailTest(String src) {
        assertThatThrownBy(() -> new Email(src))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이메일 형식이 올바른 경우 예외가 발생하지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {"asdasd@naver.com", "aa@aa.aaa", "a@name.com", "aa@aa.com", "asd@tttt.co.kr"})
    void emailFormatSuccessTest(String src) {
        assertThatNoException().isThrownBy(() -> new Email(src));
    }
}
