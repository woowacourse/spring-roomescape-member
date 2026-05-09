package roomescape.theme.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class ThemeTest {

    @Test
    void 테마_이름이_비어있으면_검증에_실패한다() {
        assertThatThrownBy(() -> Theme.of("", "설명", "https://example.com/theme.png", Duration.ofHours(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테마_설명이_비어있으면_검증에_실패한다() {
        assertThatThrownBy(() -> Theme.of("테마", "", "https://example.com/theme.png", Duration.ofHours(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테마_썸네일이_비어있으면_검증에_실패한다() {
        assertThatThrownBy(() -> Theme.of("테마", "설명", "", Duration.ofHours(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테마_진행시간이_null이면_검증에_실패한다() {
        assertThatThrownBy(() -> Theme.of("테마", "설명", "https://example.com/theme.png", null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
