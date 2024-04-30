package roomescape.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

class ReservationResponseTest {

    @Test
    @DisplayName("Reservation 객체로 ReservationResponse를 만든다.")
    void createReservationResponse() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, new Name("레벨2"), "레벨2다", "설명 없다.");
        Reservation reservation = new Reservation(
                1L,
                new Name("brown"),
                LocalDate.of(2024, 4, 1),
                time,
                theme
        );
        ReservationResponse expected = new ReservationResponse(
                1L,
                "brown",
                "2024-04-01",
                ReservationTimeResponse.from(time),
                ThemeResponse.from(theme)
        );

        ReservationResponse reservationResponse = ReservationResponse.from(reservation);

        assertThat(reservationResponse).isEqualTo(expected);
    }
}
