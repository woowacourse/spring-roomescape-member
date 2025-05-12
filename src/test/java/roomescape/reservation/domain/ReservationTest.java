package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionCause;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;


class ReservationTest {

    @DisplayName("예약시점이 과거인 경우 예외를 발생한다.")
    @Test
    void createReservationThrowExceptionIfDateIsPast() {

        // given
        LocalDateTime dateTimePast = LocalDateTime.now().minusHours(1);
        LocalDate date = dateTimePast.toLocalDate();
        ReservationTime time = ReservationTime.create(dateTimePast.toLocalTime());
        Theme theme = Theme.load(1L, "test", "test", "test");
        Member member = new Member(1L, "test", "test@test.com", "password123!", Role.USER);

        // when & then
        assertThatThrownBy(() -> Reservation.create(date, member, time, theme))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionCause.RESERVATION_IMPOSSIBLE_FOR_PAST.getMessage());
    }
}
