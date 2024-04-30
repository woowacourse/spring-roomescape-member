package roomescape.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ClientName;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.ThemeDescription;
import roomescape.domain.ThemeName;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationResponseTest {

    @DisplayName("Reservation을 입력받으면 ReservationResponse로 변환한다.")
    @Test
    void convertDtoTest() {
        // Given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(2, 22));
        final Theme theme = new Theme(1L, new ThemeName("테바의 비밀친구"), new ThemeDescription("테바의 은밀한 비밀친구"), "대충 테바 사진 링크");
        Reservation reservation = new Reservation(
                1L,
                new ClientName("켈리"),
                LocalDate.now().plusDays(1),
                reservationTime,
                theme
        );

        // When
        ReservationResponse reservationResponse = ReservationResponse.from(reservation);

        // Then
        assertAll(
                () -> assertThat(reservationResponse).isNotNull(),
                () -> assertThat(reservationResponse.id()).isEqualTo(reservation.getId()),
                () -> assertThat(reservationResponse.name()).isEqualTo(reservation.getClientName().getValue()),
                () -> assertThat(reservationResponse.date()).isEqualTo(reservation.getDate()),
                () -> assertThat(reservationResponse.time().startAt()).isEqualTo(reservation.getTime().getStartAt())
        );
    }
}
