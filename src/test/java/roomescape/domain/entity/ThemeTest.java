package roomescape.domain.entity;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ThemeTest {

    @DisplayName("동일한 ID를 가진 경우 equals/hashCode가 동일하게 작동한다")
    @ParameterizedTest
    @MethodSource("provideThemesForEquality")
    void equalsAndHashCode(Theme t1, Theme t2, boolean expected) {
        assertSoftly(soft -> {
            soft.assertThat(t1.equals(t2)).isEqualTo(expected);
            if (expected) {
                soft.assertThat(t1.hashCode()).isEqualTo(t2.hashCode());
            }
        });
    }

    static Stream<Arguments> provideThemesForEquality() {
        return Stream.of(
                Arguments.of(new Theme(1L, "a", "desc", "thumb"),
                        new Theme(1L, "b", "desc2", "thumb2"), true),
                Arguments.of(new Theme(1L, "a", "desc", "thumb"),
                        new Theme(2L, "a", "desc", "thumb"), false),
                Arguments.of(new Theme(null, "a", "desc", "thumb"),
                        new Theme(null, "a", "desc", "thumb"), false)
        );
    }
}
