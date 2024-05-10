package roomescape.reservation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.handler.exception.CustomException;
import roomescape.member.domain.Member;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    private final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 30));
    private final Theme theme = new Theme(1L, "themeName", "description", "thumbnail");
    private final LocalDateTime createdAt = LocalDateTime.of(2024, 5, 8, 12, 30);
    private final Member member = new Member(1L, "space", "abcd@gmail.com", "2580");

    @DisplayName("실패: 생성 시간보다 예약 시간이 과거일 수 없다")
    @Test
    void validateDateTimeFailTest() {
        assertThatThrownBy(() -> {
            new Reservation(1L, member, LocalDate.of(2999, 12, 12), reservationTime, theme,
                    LocalDateTime.of(3000, 12, 12, 12, 30));
        }).isInstanceOf(CustomException.class);
    }

    @DisplayName("성공: 생성 시간보다 예약 시간이 과거일 수 없다")
    @Test
    void validateDateTimeSuccessTest() {
        assertThatCode(() -> {
            new Reservation(1L, member, LocalDate.of(2999, 12, 12), reservationTime, theme,
                    LocalDateTime.of(2998, 12, 12, 12, 30));
        }).doesNotThrowAnyException();
    }

    @DisplayName("같은 날짜/시간임을 확인할 수 있다.")
    @Test
    void sameDateTimeTest() {
        Reservation reservation1 = new Reservation(1L, member, LocalDate.of(2999, 12, 12), reservationTime, theme, createdAt);
        Reservation reservation2 = new Reservation(2L, member, LocalDate.of(2999, 12, 12), reservationTime, theme, createdAt);

        assertThat(reservation1.isSameDateTime(reservation2)).isTrue();
    }

    @DisplayName("다른 날짜임을 확인할 수 있다.")
    @Test
    void differentDateTest() {
        Reservation reservation1 = new Reservation(1L, member, LocalDate.of(2999, 12, 12), reservationTime, theme, createdAt);
        Reservation reservation2 = new Reservation(2L, member, LocalDate.of(2999, 12, 11), reservationTime, theme, createdAt);

        assertThat(reservation1.isSameDateTime(reservation2)).isFalse();
    }

    @DisplayName("다른 시간임을 확인할 수 있다.")
    @Test
    void differentTimeTest() {
        ReservationTime otherReservationTime = new ReservationTime(2L, LocalTime.of(11,10));
        Reservation reservation1 = new Reservation(1L, member, LocalDate.of(2999, 12, 12), otherReservationTime, theme, createdAt);
        Reservation reservation2 = new Reservation(2L, member, LocalDate.of(2999, 12, 12), reservationTime, theme, createdAt);

        assertThat(reservation1.isSameDateTime(reservation2)).isFalse();
    }
}
