package roomescape.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.member.domain.enums.Role;

class MemberTest {

    @DisplayName("필수 값에 null이 들어가면 예외가 발생한다")
    @MethodSource
    @ParameterizedTest
    void validate_test(String name, String email, String password, Role role, String errorMessage) {
        // given
        Long id = 1L;

        // when & then
        assertThatThrownBy(() -> new Member(id, name, email, password, role))
                .isInstanceOf(NullPointerException.class)
                .hasMessage(errorMessage);
    }

    static Stream<Arguments> validate_test() {
        return Stream.of(
                Arguments.of(null, "rookie@email.com", "rookie123", Role.USER, "이름은 null일 수 없습니다."),
                Arguments.of("루키", null, "rookie123", Role.USER, "이메일은 null일 수 없습니다."),
                Arguments.of("루키", "rookie@email.com", null, Role.USER, "비밀번호는 null일 수 없습니다."),
                Arguments.of("루키", "rookie@email.com", "rookie123", null, "권한은 null일 수 없습니다.")
        );
    }

}
