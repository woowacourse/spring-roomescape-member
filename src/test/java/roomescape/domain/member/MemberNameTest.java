package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MemberNameTest {

    @ParameterizedTest
    @ValueSource(strings = {" ", "  ", "   "})
    @DisplayName("사용자 이름이 공백이면 예외를 발생한다.")
    void validateMemberName(String given) {
        //given
        MemberEmail memberEmail = new MemberEmail(given + "@coo.kr");

        //when //then
        assertThatThrownBy(() -> new MemberName(memberEmail))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
