package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MemberTest {

    @ParameterizedTest
    @CsvSource({
            "success, false",
            "fail, true"
    })
    @DisplayName("패스워드가 불일치 여부를 판단한다")
    void matchesPasswordReturnsTrueWhenMatched(String password, boolean expected) {
        // given
        Member member = new Member("name", "email@email.com", "success");

        // when
        boolean result = member.notMatchesPassword(password);

        // then
        assertThat(result)
                .isEqualTo(expected);
    }
}
