package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberNameTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "   "})
    @DisplayName("사용자 이름이 공백이면 예외를 발생한다.")
    void validateMemberName(String given) {
        //when //then
        assertThatThrownBy(() -> new MemberName(given))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
