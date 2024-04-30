package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @DisplayName("인덱스를 입력하면 해당 아이디를 가진 Reservation 객체를 생성해서 반환한다.")
    @Test
    void initializeIndex() {
        // Given
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(2, 22));
        Reservation reservation = new Reservation(
                new ClientName("켈리"),
                LocalDate.now().plusDays(1),
                reservationTime);

        Long initialIndex = 3L;

        // When
        Reservation initIndexReservation = reservation.initializeIndex(initialIndex);

        // Then
        assertThat(initIndexReservation.getId()).isEqualTo(initialIndex);
    }

    @DisplayName("현재 날짜/시간보다 이전의 예약 정보를 입력하면 예외가 발생한다.")
    @Test
    void throwExceptionWithReservationDateTimeBeforeNow() {
        // Given
        ReservationTime reservationTime = new ReservationTime(LocalTime.now());
        ClientName clientName = new ClientName("켈리");
        LocalDate dateBeforeNow = LocalDate.now().minusDays(1);

        // When & Then
        assertThatThrownBy(() -> new Reservation(clientName, dateBeforeNow, reservationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현재 날짜보다 이전 날짜를 예약할 수 없습니다.");
    }

    @DisplayName("공백의 날짜 정보를 입력하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenInputEmptyDate() {
        // Given
        final ClientName clientName = new ClientName("켈리");
        final LocalDate date = null;
        final ReservationTime reservationTime = new ReservationTime(LocalTime.now());

        // When & Then
        assertThatThrownBy(() -> new Reservation(clientName, date, reservationTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("날짜 정보는 공백을 입력할 수 없습니다.");
    }
}
