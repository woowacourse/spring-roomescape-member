package roomescape.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.domain.Role;
import roomescape.global.exception.RoomEscapeException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @ParameterizedTest
    @CsvSource(value = {",password", "tenny@wooteco.com,"})
    public void user_NullEmailOrPassword_ThrownException(String email, String password) {
        assertThatThrownBy(() -> new Member(0L, "테니", email, password, Role.MEMBER))
                .isInstanceOf(RoomEscapeException.class);
    }
}
