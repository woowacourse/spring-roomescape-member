package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MemberTest {

    @DisplayName("생성 테스트")
    @Test
    void create() {
        assertThatCode(() -> new Member("hihi", new MemberEmail("jiad1@gmail.com"), new MemberPassword("123213")))
            .doesNotThrowAnyException();
    }

    @DisplayName("이름이 빈 값이면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void create_ByBlankName(String name) {
        assertThatThrownBy(() -> new Member(name, new MemberEmail("jiad1@gmail.com"), new MemberPassword("123213")))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이메일 또는 비밀 번호가 null이면 예외가 발생한다.")
    @Test
    void create_ByNullEmailOrPassword() {
        assertThatThrownBy(() -> new Member("name", null, new MemberPassword("123213")))
            .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Member("name", new MemberEmail("jiad1@gmail.com"), null))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
