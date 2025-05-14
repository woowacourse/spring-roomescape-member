package roomescape.domain.auth.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.common.exception.InvalidArgumentException;
import roomescape.domain.auth.service.PasswordEncryptor;
import roomescape.infrastructure.security.Sha256PasswordEncryptor;

class PasswordTest {

    private final PasswordEncryptor passwordEncryptor = new Sha256PasswordEncryptor();

    @DisplayName("패스워드가 25자 이상이거나 공백이라면 예외를 반환한다.")
    @NullAndEmptySource
    @ParameterizedTest
    @ValueSource(strings = {"aaaaabbbbbcccccdddddeeee26"})
    void passwordTest1(final String password) {
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            Password.encrypt(password, passwordEncryptor);
        }).isInstanceOf(InvalidArgumentException.class);
    }
}
