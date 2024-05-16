package roomescape.domain.reservation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.member.MemberInfo;
import roomescape.domain.member.Role;

class ReservationTest {

    @DisplayName("지난 날짜로는 생성할 수 없다.")
    @Test
    void createWithPastDate() {
        // given
        String name = "name";
        ReservationTime time = new ReservationTime(1L, LocalTime.now());
        ReservationTheme theme = new ReservationTheme(1L, "name", "desc", "thumb");
        MemberInfo member = new MemberInfo(1L, "username", Role.USER);

        // when
        LocalDate date = LocalDate.now().minusDays(1);

        // then
        assertThatThrownBy(() -> new Reservation(date, time, theme, member, Purpose.CREATE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("같은 날이어도 지난 시간으로는 생성할 수 없다.")
    @Test
    void createWithPastTime() {
        // given
        String name = "name";
        LocalDate date = LocalDate.now();
        ReservationTheme theme = new ReservationTheme(1L, "name", "desc", "thumb");
        MemberInfo member = new MemberInfo(1L, "username", Role.USER);

        // when
        ReservationTime time = new ReservationTime(1L, LocalTime.now().minusMinutes(1));

        // then
        assertThatThrownBy(() -> new Reservation(date, time, theme, member, Purpose.CREATE))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
