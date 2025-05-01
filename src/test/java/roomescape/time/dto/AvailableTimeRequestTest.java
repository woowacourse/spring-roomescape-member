package roomescape.time.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class AvailableTimeRequestTest {

    @Test
    @DisplayName("날짜가 null이면 예외가 발생한다")
    void test1() {
        assertThatThrownBy(() -> new AvailableTimeRequest(null, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 날짜를 입력해주세요.");
    }

    @Test
    @DisplayName("날짜가 현재 날짜 이전이면 예외가 발생한다")
    void test2() {
        // given
        LocalDate pastDate = LocalDate.now().minusDays(1);

        // when & then
        assertThatThrownBy(() -> new AvailableTimeRequest(pastDate, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이전 날짜 값은 입력할 수 없습니다.");
    }

    @Test
    @DisplayName("테마 id가 null이면 예외가 발생한다")
    void test3() {
        // given & when & then
        assertThatThrownBy(() -> new AvailableTimeRequest(LocalDate.now(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 테마 id를 입력해주세요.");
    }

    @Test
    @DisplayName("날짜와 테마 id가 모두 유효하면 정상적으로 생성된다")
    void test4() {
        // given
        LocalDate validDate = LocalDate.now().plusDays(1);
        Long validThemeId = 1L;

        // when
        AvailableTimeRequest request = new AvailableTimeRequest(validDate, validThemeId);

        // then
        assertThat(request.date()).isEqualTo(validDate);
        assertThat(request.themeId()).isEqualTo(validThemeId);
    }
}
