package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DescriptionTest {

    @Test
    @DisplayName("생성 테스트")
    void create() {
        assertThatCode(() -> new Description("description"))
                .doesNotThrowAnyException();
    }
}
