package roomescape.domain.reservation.dto.request;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReservationCreateRequestDtoTest {

    @Nested
    @DisplayName("생성 테스트")
    class Constructor {

        @Test
        @DisplayName("정상 테스트")
        void 성공() {
            String name = "이름";
            LocalDate date = LocalDate.of(2026, 1, 1);
            Long timeId = 1L;
            Long themeId = 1L;

            assertThatCode(() -> new ReservationCreateRequestDto(name, date, timeId, themeId))
                .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("이름이 빈 문자열인 경우 예외가 발생한다.")
        void 실패1() {
            String name = "";
            LocalDate date = LocalDate.of(2026, 1, 1);
            Long timeId = 1L;
            Long themeId = 1L;

            assertThatThrownBy(() -> new ReservationCreateRequestDto(name, date, timeId, themeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 빈 문자열일 수 없습니다.");
        }
    }

}