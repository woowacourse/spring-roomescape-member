package roomescape.reservation.dto;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationRequestTest {
    @Test
    @DisplayName("모든 값이 유효하면 예약 요청 객체가 정상적으로 생성된다")
    void test1() {
        // given
        String name = "미미";
        LocalDate date = LocalDate.now().plusDays(1);
        Long timeId = 1L;
        Long themeId = 2L;

        // when & then
        assertThatCode(() -> new ReservationRequest(name, date, timeId, themeId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("이름이 null이면 예외가 발생한다")
    void test2() {
        assertThatThrownBy(() ->
                new ReservationRequest(null, LocalDate.now().plusDays(1), 1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름을 입력해주세요.");
    }

    @Test
    @DisplayName("이름이 공백이면 예외가 발생한다")
    void test3() {
        assertThatThrownBy(() ->
                new ReservationRequest("   ", LocalDate.now().plusDays(1), 1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름을 입력해주세요.");
    }

    @Test
    @DisplayName("날짜가 null이면 예외가 발생한다")
    void test4() {
        assertThatThrownBy(() ->
                new ReservationRequest("미미", null, 1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 날짜를 입력해주세요.");
    }

    @Test
    @DisplayName("과거 날짜로 예약하면 예외가 발생한다")
    void test5() {
        // given
        LocalDate pastDate = LocalDate.now().minusDays(1);

        // when & then
        assertThatThrownBy(() ->
                new ReservationRequest("미미", pastDate, 1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 지난 날짜로는 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("시간 id가 null이면 예외가 발생한다")
    void test6() {
        assertThatThrownBy(() ->
                new ReservationRequest("미미", LocalDate.now().plusDays(1), null, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 시간 id를 입력해주세요.");
    }

    @Test
    @DisplayName("테마 id가 null이면 예외가 발생한다")
    void test7() {
        assertThatThrownBy(() ->
                new ReservationRequest("미미", LocalDate.now().plusDays(1), 1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 테마 id를 입력해주세요.");
    }

}
