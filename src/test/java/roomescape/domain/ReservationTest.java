package roomescape.domain;

import org.junit.jupiter.api.Test;
import roomescape.common.exception.DomainException;
import roomescape.domain.vo.Description;
import roomescape.domain.vo.Name;
import roomescape.domain.vo.ThumbnailUrl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {
    private final Name name = new Name("브라운");
    private final ThumbnailUrl url = new ThumbnailUrl("https://test1.com");
    private final Description description = new Description("설명을 테스트 해야 하는 테스트 설명");
    private final Theme theme = new Theme(1L, new Name("테마입니다"), url, description);
    private final Time time = new Time(1L, LocalTime.of(14, 0));
    private final LocalDateTime fixedNow = LocalDateTime.of(2026, 5, 10, 12, 0);


    @Test
    void 과거_날짜는_예약_불가() {
        LocalDate past = LocalDate.of(2026, 5, 9);

        assertThatThrownBy(() ->
                Reservation.create(name, past, time, theme, fixedNow))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void 정확히_14일_뒤는_가능() {
        LocalDate exact14 = LocalDate.of(2026, 5, 24);

        assertThatNoException().isThrownBy(() ->
                Reservation.create(name, exact14, time, theme, fixedNow));
    }

    @Test
    void _15일_뒤는_불가() {
        LocalDate day15 = LocalDate.of(2026, 5, 25);

        assertThatThrownBy(() ->
                Reservation.create(name, day15, time, theme, fixedNow))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("14일");
    }

    @Test
    void 오늘_이미_지난_시각은_불가() {
        LocalDate today = LocalDate.of(2026, 5, 10);
        Time noonTime = new Time(1L, LocalTime.of(11, 0));

        assertThatThrownBy(() ->
                Reservation.create(name, today, noonTime, theme, fixedNow))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void 오늘_안_지난_시각은_가능() {
        LocalDate today = LocalDate.of(2026, 5, 10);
        Time eveningTime = new Time(1L, LocalTime.of(20, 0));

        assertThatNoException().isThrownBy(() ->
                Reservation.create(name, today, eveningTime, theme, fixedNow));
    }

    @Test
    void 미래_날짜는_시간_상관없이_가능() {
        LocalDate tomorrow = LocalDate.of(2026, 5, 11);
        Time anyTime = new Time(1L, LocalTime.of(10, 0));

        assertThatNoException().isThrownBy(() ->
                Reservation.create(name, tomorrow, anyTime, theme, fixedNow));
    }
}