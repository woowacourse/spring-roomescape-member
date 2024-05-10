package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.exception.InvalidClientRequestException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @DisplayName("이메일, 이름이 공백이면 예외를 발생시킨다.")
    @CsvSource({"email@email.com,", ",test"})
    @ParameterizedTest
    void given_emailWithName_when_newWithEmptyValue_then_thrownException(String email, String name) {
        assertThatThrownBy(() -> new Member(1L, email, name)).isInstanceOf(InvalidClientRequestException.class);
    }

    @DisplayName("Id가 0 이하이면 예외를 발생시킨다.")
    @Test
    void given_when_newWithZeroValue_then_thrownException() {
        assertThatThrownBy(() -> new Member(0L, "poke@test.com", "poke")).isInstanceOf(InvalidClientRequestException.class);
    }
}