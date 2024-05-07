package roomescape.domain.theme;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ThumbnailTest {
    @Test
    @DisplayName("테마 썸네일이 비어있다면 예외가 발생한다")
    void blank() {
        Assertions.assertThatThrownBy(() -> new Thumbnail("")).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("썸네일은 비어있을 수 없습니다.");
    }
}
