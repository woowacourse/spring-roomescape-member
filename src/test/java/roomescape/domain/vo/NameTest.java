package roomescape.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Named.named;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class NameTest {

    @Nested
    class Constructor {

        static Stream<Named<String>> validNames() {
            return Stream.of(
                    named("최소 길이 (2글자)", "AB"),
                    named("최대 길이 (15글자)", "A".repeat(15))
            );
        }

        static Stream<Named<String>> invalidNames() {
            return Stream.of(
                    named("공백", "   "),
                    named("최소 길이 미만 (1글자)", "A"),
                    named("최대 길이 초과 (16글자)", "A".repeat(16))
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("validNames")
        @DisplayName("경계값으로 생성에 성공한다")
        void createsName(String value) {
            assertThat(new Name(value).getValue()).isEqualTo(value);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidNames")
        @DisplayName("유효하지 않은 이름이면 예외를 던진다")
        void throwsWhenInvalid(String value) {
            assertThatThrownBy(() -> new Name(value))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
