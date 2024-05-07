package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @Test
    @DisplayName("이메일에서 이름을 추출한다.")
    void extract() {
        //given
        String given = "abcd";
        String email = given + "@test.com";

        //when
        MemberEmail memberEmail = new MemberEmail(email);
        String result = memberEmail.extractName();

        //then
        assertThat(result).isEqualTo(given);
    }
}
