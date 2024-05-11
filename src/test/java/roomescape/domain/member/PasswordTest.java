package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class PasswordTest {
    @Test
    void 비밀번호를_생성한다() {
        String password = "1q2w3e4r!@";

        Password actual = new Password(password);

        assertThat(actual.getValue()).isEqualTo(password);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 비밀번호가_비어있으면_예외가_발생한다(String password) {
        assertThatThrownBy(() -> new Password(password))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호가 비어있습니다.");
    }

    @Test
    void 비밀번호가_최소_길이보다_짧으면_예외가_발생한다() {
        String password = "1234567";
        assertThatThrownBy(() -> new Password(password))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호는 8글자 이상, 20글자 이하여야 합니다.");
    }

    @Test
    void 비밀번호가_최대_길이보다_길면_예외가_발생한다() {
        String password = "123456789012345678901";
        assertThatThrownBy(() -> new Password(password))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호는 8글자 이상, 20글자 이하여야 합니다.");
    }

    @Test
    void 비밀번호에_공백이_포함되어_있으면_예외가_발생한다() {
        String password = "123 45678";
        assertThatThrownBy(() -> new Password(password))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호에 공백이 포함되어 있습니다.");
    }

    @Test
    void 비밀번호에_영어_숫자_특수문자_이외의_문자가_존재하면_예외가_발생한다() {
        String password = "비밀번호486!";
        assertThatThrownBy(() -> new Password(password))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호는 영어, 숫자, 특수문자만 가능합니다.");
    }

    @ParameterizedTest(name = "비교할 비밀번호: \"{0}\", 결과: {1}")
    @CsvSource(value = {"1q2w3e4r!@,true", "abcdabcd,false"})
    void 비밀번호가_일치하는지_확인한다(String rawPassword, boolean expected) {
        Password password = new Password("1q2w3e4r!@");

        assertThat(password.match(rawPassword)).isEqualTo(expected);
    }
}
