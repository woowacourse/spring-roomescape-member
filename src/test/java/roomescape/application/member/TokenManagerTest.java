package roomescape.application.member;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.application.ServiceTest;

@ServiceTest
class TokenManagerTest {

    @Autowired
    private TokenManager tokenManager;

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("토큰이 비어있는 경우, 예외를 발생한다.")
    void invalidTokenOnBlankOrNull(String token) {
        assertThatCode(() -> tokenManager.parseToken(token))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("토큰 값이 없습니다.");
    }
}
