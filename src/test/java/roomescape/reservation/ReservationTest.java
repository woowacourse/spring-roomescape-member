package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.exception.RoomescapeException;
import roomescape.theme.Theme;
import roomescape.time.ReservationTime;

class ReservationTest {

    private final Theme theme = new Theme(1L, "공포의 방", "설명", "thumb.jpg");
    private final ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
    private final LocalDateTime now = LocalDateTime.of(2026, 5, 14, 12, 0);

    @Test
    void 미래_예약_생성() {
        LocalDate date = LocalDate.of(2026, 12, 31);

        assertThatCode(() -> new Reservation("동키", theme, date, time, now))
                .doesNotThrowAnyException();
    }

    @Test
    void 과거_날짜_예약시_예외() {
        LocalDate pastDate = LocalDate.of(2024, 1, 1);

        assertThatThrownBy(() -> new Reservation("동키", theme, pastDate, time, now))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 오늘_지난_시간_예약시_예외() {
        LocalDate today = LocalDate.of(2026, 5, 14);

        assertThatThrownBy(() -> new Reservation("동키", theme, today, time, now))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 활성_예약_취소() {
        Reservation reservation = new Reservation(1L, "동키", theme, LocalDate.of(2026, 12, 31), time, null);

        reservation.cancel(now);

        assertThat(reservation.getDeletedAt()).isEqualTo(now);
    }

    @Test
    void 이미_취소된_예약_재취소시_예외() {
        Reservation reservation = new Reservation(1L, "동키", theme, LocalDate.of(2026, 12, 31), time,
                LocalDateTime.of(2026, 1, 1, 0, 0));

        assertThatThrownBy(() -> reservation.cancel(now))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 지난_예약_취소시_예외() {
        Reservation reservation = new Reservation(1L, "동키", theme, LocalDate.of(2024, 1, 1), time, null);

        assertThatThrownBy(() -> reservation.cancel(now))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 예약_변경시_id_유지() {
        Reservation reservation = new Reservation(100L, "동키", theme, LocalDate.of(2026, 12, 31), time, null);
        Theme newTheme = new Theme(2L, "미스터리", "설명", "thumb.jpg");
        ReservationTime newTime = new ReservationTime(2L, LocalTime.of(14, 0));
        LocalDate newDate = LocalDate.of(2027, 1, 1);

        Reservation updated = reservation.change(newTheme, newDate, newTime, now);

        assertThat(updated.getId()).isEqualTo(100L);
        assertThat(updated.getTheme()).isEqualTo(newTheme);
        assertThat(updated.getDate()).isEqualTo(newDate);
        assertThat(updated.getTime()).isEqualTo(newTime);
    }

    @Test
    void 이미_취소된_예약_변경시_예외() {
        Reservation reservation = new Reservation(1L, "동키", theme, LocalDate.of(2026, 12, 31), time,
                LocalDateTime.of(2026, 1, 1, 0, 0));

        assertThatThrownBy(() -> reservation.change(theme, LocalDate.of(2027, 1, 1), time, now))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 본인_예약_확인() {
        Reservation reservation = new Reservation(1L, "동키", theme, LocalDate.of(2026, 12, 31), time, null);

        assertThat(reservation.belongsTo("동키")).isTrue();
        assertThat(reservation.belongsTo("그해")).isFalse();
    }
}
