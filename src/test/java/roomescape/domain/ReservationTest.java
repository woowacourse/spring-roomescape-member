package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.model.ReservationTime;
import roomescape.reservation.model.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationTest {

    @DisplayName("인덱스를 입력하면 해당 아이디를 가진 Reservation 객체를 생성해서 반환한다.")
    @Test
    void initializeIndex() {
        // Given
        final String clientName = "켈리";
        final LocalDate reservationDate = LocalDate.now().plusDays(1);
        final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(2, 22));
        final Theme theme = Theme.of(1L, "테바의 비밀친구", "테바의 은밀한 비밀친구", "대충 테바 사진 링크");
        final Reservation reservation = Reservation.of(
                clientName,
                reservationDate,
                reservationTime,
                theme
        );
        final Long initialIndex = 3L;

        // When
        final Reservation initIndexReservation = reservation.initializeIndex(initialIndex);

        // Then
        assertThat(initIndexReservation.getId()).isEqualTo(initialIndex);
    }
}
