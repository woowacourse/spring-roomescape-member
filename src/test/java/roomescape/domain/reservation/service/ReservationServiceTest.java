package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.request.ReservationCreateRequest;
import roomescape.domain.reservation.response.ReservationResponse;
import roomescape.domain.reservation.response.ReservationTimeResponse;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    ReservationService reservationService;

    @Test
    @DisplayName("예약을 성공적으로 생성한다.")
    void saveReservation() {
        // given
        Long timeId = 1L;
        ReservationTime reservationTime = new ReservationTime(
                timeId, LocalTime.of(10, 0)
        );

        when(reservationTimeRepository.findById(eq(timeId)))
                .thenReturn(Optional.of(reservationTime));

        Long reservationId = 1L;
        Reservation reservation = new Reservation(
                reservationId,
                "브라운",
                LocalDate.of(2026, 4, 30),
                reservationTime
        );

        when(reservationRepository.save(any(Reservation.class)))
                .thenReturn(reservation);

        ReservationCreateRequest request = new ReservationCreateRequest(
                "브라운",
                LocalDate.of(2026, 4, 30),
                timeId
        );

        // when
        ReservationResponse response = reservationService.saveReservation(request);

        // then
        assertThat(response.id()).isEqualTo(reservationId);
        assertThat(response.name()).isEqualTo("브라운");
        assertThat(response.date()).isEqualTo(LocalDate.of(2026, 4, 30));
        assertThat(response.time().id()).isEqualTo(timeId);

        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    @DisplayName("존재하지 않는 시간으로 예약 시 예외가 발생한다.")
    void saveReservation_throwsException_whenTimeNotFound() {
        // given
        Long invalidTimeId = 999L;

        ReservationCreateRequest request = new ReservationCreateRequest(
                "브라운",
                LocalDate.of(2026, 4, 30),
                invalidTimeId
        );

        when(reservationTimeRepository.findById(invalidTimeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.saveReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 id의 ReservationTime이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("모든 예약을 조회한다.")
    void findAllReservations() {
        // given
        List<Reservation> reservations = new ArrayList<>();
        ReservationTime time1 = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime time2 = new ReservationTime(2L, LocalTime.of(11, 0));

        reservations.add(new Reservation(1L, "브라운", LocalDate.of(2026, 4, 30), time1));
        reservations.add(new Reservation(2L, "크루", LocalDate.of(2026, 4, 30), time2));
        when(reservationRepository.findAll()).thenReturn(reservations);

        // when
        List<ReservationResponse> responses = reservationService.findAllReservations();

        // then
        assertThat(responses).hasSize(2)
                .extracting("name", "date", "time")
                .containsExactly(
                        tuple("브라운", LocalDate.of(2026, 4, 30), ReservationTimeResponse.from(time1)),
                        tuple("크루", LocalDate.of(2026, 4, 30), ReservationTimeResponse.from(time2))
                );

        verify(reservationRepository).findAll();
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteReservationBy() {
        // given
        Long reservationId = 1L;

        // when
        reservationService.deleteReservationBy(reservationId);

        // then
        verify(reservationRepository).deleteById(reservationId);
    }
}
