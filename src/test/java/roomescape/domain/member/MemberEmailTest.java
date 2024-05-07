package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberEmailTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "abr", "12341231"})
    @DisplayName("이메일 형식이 아닌 입력값은 예외가 발생한다.")
    void validateEmail(String input) {
        //when //then
        assertThatThrownBy(() -> new MemberEmail(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
