package roomescape.business.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlayTimeTest {

    @Test
    @DisplayName("startAt 필드에 null이 들어오면 예외가 발생한다")
    void validateStartAt() {
        // given
        final LocalTime invalidStartAt = null;

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> new PlayTime(invalidStartAt))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> new PlayTime(1L, invalidStartAt))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("id를 포함하여 생성할 때 id에 null은 들어올 수 없다.")
    @Test
    void createWithId() {
        // given
        final Long invalidId = null;
        final LocalTime validStartAt = LocalTime.MAX;

        // when & then
        assertThatThrownBy(() -> PlayTime.createWithId(invalidId, validStartAt))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
