package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationThemeTest {

    @DisplayName("테마명의 사이즈가 최댓값 이내로 입력되었는지 검증한다.")
    @Test
    void validateThemeNameSize() {
        // given
        String validName = "12345678901234567890";
        String invalidName = "123456789012345678901";

        // when
        ReservationTheme theme = new ReservationTheme(validName, "", "");

        // then
        assertThat(theme.getName()).isEqualTo("12345678901234567890");
        assertThatThrownBy(() -> new ReservationTheme(invalidName, "", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 테마명은 20글자 이내로 입력해 주세요. 현재 길이는 21글자 입니다.");
    }

}
