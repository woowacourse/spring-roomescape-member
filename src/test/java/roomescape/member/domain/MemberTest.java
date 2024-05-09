package roomescape.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import roomescape.exception.BadRequestException;

import static org.assertj.core.api.Assertions.*;

@DisplayName("사용자")
class MemberTest {

    @DisplayName("사용자는 필드에 빈 값이 있으면 예외가 발생한다.")
    @NullAndEmptySource
    @ParameterizedTest
    void validateNullOrEmptyField(String blank) {
        // when & then
        assertThatThrownBy(() -> new Member(blank, "email", "password"))
                .isInstanceOf(BadRequestException.class);
    }
}
