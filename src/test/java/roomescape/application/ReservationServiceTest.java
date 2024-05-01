package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.PlayerName;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private Clock clock;

    @DisplayName("정상적인 예약 요청을 받아서 저장하고, 예약 응답을 반환한다.")
    @Test
    void shouldReturnReservationResponseWhenValidReservationRequestSave() {
        ReservationRequest reservationRequest = new ReservationRequest("test", LocalDate.of(2024, 12, 26), 1L);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));
        Reservation reservation = new Reservation(
                1L,
                new PlayerName("test"),
                reservationRequest.date(),
                reservationTime
        );

        setClockMock(2024, 12, 25, 0, 0);
        given(reservationTimeRepository.findById(any(Long.class)))
                .willReturn(Optional.of(reservationTime));
        given(reservationRepository.existByDateAndTimeId(any(LocalDate.class), any(Long.class)))
                .willReturn(false);
        given(reservationRepository.create(any(Reservation.class)))
                .willReturn(reservation);

        reservationService.create(reservationRequest);

        then(reservationTimeRepository).should(times(1)).findById(any(Long.class));
        then(reservationRepository).should(times(1))
                .existByDateAndTimeId(any(LocalDate.class), any(Long.class));
        then(reservationRepository).should(times(1)).create(any(Reservation.class));
    }

    @DisplayName("존재하지 않는 예약 시간으로 예약을 생성시 IllegalArgumentException 예외를 반환한다.")
    @Test
    void shouldReturnIllegalArgumentExceptionWhenNotFoundReservationTime() {
        assertThatCode(() -> reservationService.create(new ReservationRequest("test", LocalDate.now(), 1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 시간 입니다.");

        then(reservationTimeRepository).should().findById(any(Long.class));
        then(reservationRepository).should(times(0))
                .existByDateAndTimeId(any(LocalDate.class), any(Long.class));
        then(reservationRepository).should(times(0)).create(any(Reservation.class));
    }

    @DisplayName("중복된 예약을 하는 경우 IllegalStateException 예외를 반환한다.")
    @Test
    void shouldReturnIllegalStateExceptionWhenDuplicatedReservationCreate() {
        ReservationRequest reservationRequest = new ReservationRequest("test", LocalDate.of(2024, 12, 27), 1L);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));

        setClockMock(2024, 12, 26, 0, 0);
        given(reservationTimeRepository.findById(any(Long.class)))
                .willReturn(Optional.of(reservationTime));
        given(reservationRepository.existByDateAndTimeId(any(LocalDate.class), any(Long.class)))
                .willReturn(true);

        assertThatCode(() -> reservationService.create(reservationRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 예약입니다.");

        then(reservationTimeRepository).should().findById(any(Long.class));
        then(reservationRepository).should()
                .existByDateAndTimeId(any(LocalDate.class), any(Long.class));
        then(reservationRepository).should(times(0)).create(any(Reservation.class));
    }

    @DisplayName("과거 시간을 예약하는 경우 IllegalArgumentException 예외를 반환한다.")
    @Test
    void shouldThrowsIllegalArgumentExceptionWhenReservationDateIsBeforeCurrentDate() {
        ReservationRequest reservationRequest = new ReservationRequest("test", LocalDate.of(2024, 12, 25), 1L);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(12, 0));

        setClockMock(2024, 12, 26, 0, 0);
        given(reservationTimeRepository.findById(any(Long.class)))
                .willReturn(Optional.of(reservationTime));

        assertThatCode(() -> reservationService.create(reservationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현재 시간보다 과거로 예약할 수 없습니다.");
    }

    @DisplayName("모든 예약 조회시 reservationRepository findAll 메서드를 1회 호출하고 모든 예약을 반환한다.")
    @Test
    void shouldReturnReservationResponsesWhenReservationsExist() {
        List<ReservationResponse> reservationResponses = reservationService.findAll();

        assertThat(reservationResponses).isEmpty();

        then(reservationRepository).should().findAll();
    }

    @DisplayName("예약 삭제 요청시 예약이 존재하면 예약을 삭제한다.")
    @Test
    void shouldDeleteReservationWhenReservationExist() {
        Reservation reservation = new Reservation(
                1L,
                new PlayerName("test"),
                LocalDate.now(),
                new ReservationTime(1L, LocalTime.now())
        );

        given(reservationRepository.findById(any(Long.class)))
                .willReturn(Optional.of(reservation));

        reservationService.deleteById(any(Long.class));

        then(reservationRepository).should().findById(any(Long.class));
        then(reservationRepository).should().deleteById(any(Long.class));
    }

    @DisplayName("예약 삭제 요청시 예약이 존재하지 않으면 IllegalArgumentException 예외를 반환한다.")
    @Test
    void shouldThrowsIllegalArgumentExceptionWhenReservationDoesNotExist() {
        given(reservationRepository.findById(any(Long.class)))
                .willReturn(Optional.empty());

        assertThatCode(() -> reservationService.deleteById(any(Long.class)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 입니다.");

        then(reservationRepository).should().findById(any(Long.class));
        then(reservationRepository).should(times(0)).deleteById(any(Long.class));
    }

    private void setClockMock(int year, int month, int day, int hour, int minute) {
        given(clock.instant())
                .willReturn(
                        Instant.from(LocalDateTime.of(year, month, day, hour, minute).atZone(ZoneId.systemDefault())));
        given(clock.getZone())
                .willReturn(ZoneId.systemDefault());
    }
}
