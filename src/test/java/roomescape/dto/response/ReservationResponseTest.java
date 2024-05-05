package roomescape.dto.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.InitialDataFixture;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

class ReservationResponseTest {

    @Test
    @DisplayName("Reservation 객체로 ReservationResponse를 만든다.")
    void createReservationResponse() {
        ReservationResponse expected = new ReservationResponse(
                InitialDataFixture.RESERVATION_1.getId(),
                InitialDataFixture.RESERVATION_1.getName().getName(),
                InitialDataFixture.RESERVATION_1.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                ReservationTimeResponse.from(InitialDataFixture.RESERVATION_1.getTime()),
                ThemeResponse.from(InitialDataFixture.RESERVATION_1.getTheme())
        );

        ReservationResponse reservationResponse = ReservationResponse.from(InitialDataFixture.RESERVATION_1);

        assertThat(reservationResponse).isEqualTo(expected);
    }
}
