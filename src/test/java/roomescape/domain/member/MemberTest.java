package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MemberTest {

    @ParameterizedTest
    @CsvSource({"1111, true", "1234, false"})
    @DisplayName("두 비밀번호가 일치하지 않는지 확인한다.")
    void checkIsIncorrectPassword(final String password, final boolean isIncorrect) {
        // given
        final Member member = new Member(1L, new Name("냥인"), "nyangin@email.com", "1234", Role.USER);

        // when
        final boolean actual = member.isIncorrectPassword(password);

        // then
        assertThat(actual).isEqualTo(isIncorrect);
    }
}
