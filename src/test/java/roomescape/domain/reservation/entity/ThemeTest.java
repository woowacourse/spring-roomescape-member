package roomescape.domain.reservation.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ThemeTest {

    @DisplayName("아이디 존재 여부")
    @ParameterizedTest
    @CsvSource(value = {"1,true", "null,false"}, delimiter = ',', nullValues = "null")
    void test1(final Long id, final boolean expected) {
        // given
        final Theme theme = new Theme(id, "공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        // when
        final boolean result = theme.existId();

        // then
        assertThat(result).isEqualTo(expected);
    }
}
