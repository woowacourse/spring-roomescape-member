package roomescape.reservation.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.auth.principal.AuthenticatedMember;
import roomescape.member.model.MemberRole;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.model.ReservationTime;
import roomescape.reservation.model.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationResponseTest {

    @DisplayName("Reservation을 입력받으면 ReservationResponse로 변환한다.")
    @Test
    void convertDtoTest() {
        // Given
        final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(2, 22));
        final Theme theme = Theme.of(1L, "테바의 비밀친구", "테바의 은밀한 비밀친구", "대충 테바 사진 링크");
        final String clientName = "켈리";
        final LocalDate reservationDate = LocalDate.now().plusDays(1);
        final Reservation reservation = Reservation.of(1L, clientName, reservationDate, reservationTime, theme);
        final AuthenticatedMember authenticatedMember = new AuthenticatedMember(1L, clientName, "kelly6bf@gmail.com", MemberRole.USER);

        // When
        final ReservationResponse reservationResponse = ReservationResponse.from(reservation, authenticatedMember);

        // Then
        assertAll(
                () -> assertThat(reservationResponse).isNotNull(),
                () -> assertThat(reservationResponse.id()).isEqualTo(reservation.getId()),
                () -> assertThat(reservationResponse.member().name()).isEqualTo(reservation.getClientName().value()),
                () -> assertThat(reservationResponse.date()).isEqualTo(reservation.getDate().value()),
                () -> assertThat(reservationResponse.time().startAt()).isEqualTo(reservation.getTime().getStartAt())
        );
    }
}
