package roomescape.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.ViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.TestFixture.*;

class MemberTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"123", "", " ", "miaIsLovely"})
    @DisplayName("사용자 이름은 숫자로만 구성될 수 없고 10자 이하이다.")
    void validateName(String invalidName) {
        // when & then
        assertThatThrownBy(() -> new Member(MIA_EMAIL, invalidName, TEST_PASSWORD))
                .isInstanceOf(ViolationException.class);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"aaa", "aa@aa", "aa.aa@aa"})
    @DisplayName("사용자 email의 형식은 [계정]@[도메인].[최상위도메인]이다.")
    void validateEmail(String invalidEmail) {
        // when & then
        assertThatThrownBy(() -> new Member(MIA_NAME, invalidEmail, TEST_PASSWORD))
                .isInstanceOf(ViolationException.class);
    }
}
