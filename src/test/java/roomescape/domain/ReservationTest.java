package roomescape.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.vo.ThemeName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ReservationTest {
    private static final ReservationTime TIME = new ReservationTime(1L, "13:24");
    private static final Theme THEME = new Theme(1L, new ThemeName("name"), "d", "url");

    @Test
    void 정상형식_날짜_예약_생성_테스트() {
        // when, then
        assertDoesNotThrow(() -> new Reservation("제임스", "2026-04-30", TIME, THEME));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2023.10.27", "2023/10/27", "23-10-27", "2023-1-1", "20231027",
            "2023-13-01", "2023-00-15", "2023-10-32", "2023-04-31", "2023-02-29",
            "2023-10-27 (금)", "Today", "2023-10-27 ", " 2023-10-27", "yyyy-MM-dd", ""})
    void 형식외_날짜로_예약시_예외가_발생한다(String date) {
        // when, then
        assertThatThrownBy(() -> new Reservation("제임스", date, TIME, THEME))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 동일_id_동일_객체_테스트() {
        // given
        Reservation reservation1 = new Reservation(1L, "제임스", "2026-04-30", TIME, THEME);
        Reservation reservation2 = new Reservation(1L, "포비", "2026-05-01", TIME, THEME);

        // when, then
        assertThat(reservation1).isEqualTo(reservation2);
    }

    @Test
    void 다른_id_다른_객체_테스트() {
        // given
        Reservation reservation1 = new Reservation(1L, "제임스", "2026-04-30", TIME, THEME);
        Reservation reservation2 = new Reservation(2L, "제임스", "2026-04-30", TIME, THEME);

        // when, then
        assertThat(reservation1).isNotEqualTo(reservation2);
    }
}