package roomescape.business.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PlayTimeTest {

    @DisplayName("생성자로 null은 들어올 수 없다.")
    @Test
    void validateNonNull() {
        // given
        final LocalTime invalidStartAt = null;

        // when & then
        assertThatThrownBy(() -> new PlayTime(invalidStartAt))
                .isInstanceOf(IllegalArgumentException.class);
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
