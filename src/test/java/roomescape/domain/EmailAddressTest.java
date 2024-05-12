package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.member.EmailAddress;
import roomescape.exception.ParameterException;

class EmailAddressTest {
    @DisplayName("이메일 주소 형식이 잘못된 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"hello", "Hello@", "hello@hello"})
    void emailAddressFormatTest(String address) {
        assertThatThrownBy(() -> new EmailAddress(address))
                .isInstanceOf(ParameterException.class)
                .hasMessage("올바르지 않은 이메일입니다.");
    }
}
