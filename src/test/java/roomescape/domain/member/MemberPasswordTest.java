package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberPasswordTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "     "})
    @DisplayName("비밀번호가 비어있거나 공백이면 예외를 발생한다.")
    void validateMemberPassword(String given) {
        //when //then
        assertThatThrownBy(() -> new MemberPassword(given))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
