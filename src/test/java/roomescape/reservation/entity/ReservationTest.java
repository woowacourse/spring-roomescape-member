package roomescape.reservation.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.theme.entity.Theme;

class ReservationTest {

    private static final String OWNER_NAME = "밀란";
    private static final String OTHER_NAME = "수달";

    @Test
    void 예약_이름이_같으면_예약의_주인이다() {
        // given
        Reservation reservation = reservation(OWNER_NAME);

        // when
        boolean result = reservation.isOwner(OWNER_NAME);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 예약_이름이_다르면_예약의_주인이_아니다() {
        // given
        Reservation reservation = reservation(OWNER_NAME);

        // when
        boolean result = reservation.isOwner(OTHER_NAME);

        // then
        assertThat(result).isFalse();
    }

    private Reservation reservation(String name) {
        return Reservation.create(
                name,
                LocalDate.of(2026, 5, 10),
                ReservationTime.of(1L, LocalTime.of(10, 0)),
                Theme.of(1L, "테마", "테마 설명", "https://example.com/theme.png", Theme.RUNTIME)
        );
    }

}
