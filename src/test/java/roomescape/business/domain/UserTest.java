package roomescape.business.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UserTest {

    @DisplayName("생성자에 null 값이 들어올 수 없다.")
    @ParameterizedTest
    @MethodSource("provideConstructorNullArguments")
    void validateNonNull(
            final String name,
            final String email,
            final String password,
            final Role role
    ) {
        assertThatThrownBy(() -> new User(name, email, password, role))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> provideConstructorNullArguments() {
        return Stream.of(
                Arguments.of(null, "user@example.com", "password123", Role.USER),
                Arguments.of("User", null, "password123", Role.USER),
                Arguments.of("User", "user@example.com", null, Role.USER),
                Arguments.of("User", "user@example.com", "password123", null)
        );
    }

    @DisplayName("생성자에 빈 문자열 또는 공백 문자열이 들어올 수 없다.")
    @ParameterizedTest
    @MethodSource("provideConstructorBlankArguments")
    void validateNotBlank(
            final String name,
            final String email,
            final String password
    ) {
        assertThatThrownBy(() -> new User(name, email, password, Role.USER))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> provideConstructorBlankArguments() {
        return Stream.of(
                Arguments.of("", "user@example.com", "password123"),
                Arguments.of("User", " ", "password123"),
                Arguments.of("User", "user@example.com", ""),
                Arguments.of("User", "user@example.com", "    ")
        );
    }

    @DisplayName("id를 포함하여 생성할 때 id에 null이 들어올 수 없다.")
    @ParameterizedTest
    @MethodSource("provideConstructorArguments")
    void createWithId(
            final String name,
            final String email,
            final String password,
            final Role role
    ) {
        assertAll(
                () -> assertThatThrownBy(() -> User.createWithId(null, "User", "user@example.com", "password123", Role.USER))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> User.createWithId(1L, name, email, password, role))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    private static Stream<Arguments> provideConstructorArguments() {
        return Stream.of(
                Arguments.of(null, "user@example.com", "password123", Role.USER),
                Arguments.of("User", null, "password123", Role.USER),
                Arguments.of("User", "user@example.com", null, Role.USER),
                Arguments.of("User", "user@example.com", "password123", null)
        );
    }
}
