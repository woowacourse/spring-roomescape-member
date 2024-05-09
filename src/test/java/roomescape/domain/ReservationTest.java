package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ReservationTest {

    @ParameterizedTest
    @CsvSource(value = {"1,1", "0,1", "0,0"})
    @DisplayName("신규 예약의 예약 날짜, 시간을 검증한다.")
    void validateDateTime(int minusDay, int minusMinute) {
        // given
        Member member = new Member(1L, new Name("test"), "test@gmail.com", Role.USER);
        ReservationDate date = new ReservationDate(LocalDate.now().minusDays(minusDay));
        ReservationTime time = new ReservationTime(LocalTime.now().minusMinutes(minusMinute));
        Theme theme = new Theme("테마1", "테마1 설명", "테마1 썸네일");
        Reservation reservation = new Reservation(member, date, time, theme);

        // when, then
        assertThatThrownBy(reservation::validateDateTime).isInstanceOf(IllegalStateException.class);
    }
}
