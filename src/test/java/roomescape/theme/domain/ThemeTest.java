package roomescape.theme.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ThemeTest {

    @Test
    @DisplayName("아이디 할당 테스트")
    void assignId_Test() {
        Theme withoutId = Theme.createWithoutId("a", "b", "c");

        Theme theme = withoutId.assignId(1L);

        Assertions.assertAll(
                () -> assertThat(theme.getId()).isEqualTo(1L),
                () -> assertThat(theme.getName()).isEqualTo("a"),
                () -> assertThat(theme.getDescription()).isEqualTo("b"),
                () -> assertThat(theme.getThumbnail()).isEqualTo("c")
        );
    }
}