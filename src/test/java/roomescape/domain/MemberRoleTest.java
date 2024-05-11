package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MemberRoleTest {

    @ParameterizedTest
    @MethodSource("roleSource")
    @DisplayName("이름에 대응되는 역할을 반환한다.")
    void from(String source, MemberRole expected) {
        MemberRole admin = MemberRole.from(source);

        assertThat(admin).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하지 않는 역할 이름으로 역할을 반환할 수 없다.")
    void cant() {
        assertThatThrownBy(() -> MemberRole.from("unknown"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 역할입니다.");
    }

    public static List<Arguments> roleSource() {
        return List.of(
                Arguments.arguments("ADMIN", MemberRole.ADMIN),
                Arguments.arguments("NORMAL", MemberRole.NORMAL)
        );
    }
}
