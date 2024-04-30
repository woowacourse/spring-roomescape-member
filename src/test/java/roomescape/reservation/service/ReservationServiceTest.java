package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.reservation.dto.request.CreateReservationRequest;
import roomescape.reservation.dto.response.FindReservationResponse;
import roomescape.reservation.dto.response.FindTimeOfReservationsResponse;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.model.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.util.DummyDataFixture;

@SpringBootTest
class ReservationServiceTest extends DummyDataFixture {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("예약 생성 시 해당 데이터의 id값을 반환한다.")
    void createReservation() {
        // given
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                LocalDate.of(10, 10, 10), "포비", 1L);

        // stub
        Mockito.when(reservationRepository.save(any(Reservation.class))).thenReturn(10L);
        Mockito.when(reservationTimeRepository.findById(1L))
                .thenReturn(Optional.of(new ReservationTime(null, any(LocalTime.class))));

        // when
        long reservationTimeId = reservationService.createReservation(createReservationRequest);

        // then
        assertThat(reservationTimeId).isEqualTo(10L);
    }

    @Test
    @DisplayName("예약 생성 시 해당하는 예약 시간 id와 동일하게 저장된 예약 시간이 없는 경우 예외가 발생한다.")
    void createReservation_ifNotExist_throwException() {
        // given
        CreateReservationRequest createReservationRequest = new CreateReservationRequest(
                LocalDate.of(10, 10, 10), "포비", 1L);

        // stub
        Mockito.when(reservationTimeRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(createReservationRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 예약 시간이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("예약 목록 조회 시 저장된 예약과 예약 시간에 대한 정보를 반환한다.")
    void getReservations() {
        // stub
        Mockito.when(reservationRepository.findAll())
                .thenReturn(super.getPreparedReservations());

        // when & then
        assertThat(reservationService.getReservations()).containsExactly(
                new FindReservationResponse(1L, "아서", "2024-04-24", new FindTimeOfReservationsResponse(1L, "15:40")),
                new FindReservationResponse(2L, "몰리", "2024-04-23", new FindTimeOfReservationsResponse(2L, "10:00"))
        );
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 대한 정보를 반환한다.")
    void getReservation() {
        // given
        long timeId = 1L;

        // stub
        Mockito.when(reservationRepository.findById(timeId))
                .thenReturn(Optional.ofNullable(super.getReservationById(timeId)));

        // when & then
        assertThat(reservationService.getReservation(timeId)).isEqualTo(
                new FindReservationResponse(1L, "아서", "2024-04-24", new FindTimeOfReservationsResponse(1L, "15:40")));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약이 없는 경우 예외가 발생한다.")
    void getReservation_ifNotExist_throwException() {
        // stub
        Mockito.when(reservationRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.getReservation(anyLong()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 예약이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약을 삭제한다.")
    void deleteReservation() {
        // given
        long reservationId = 1L;

        // stub
        Mockito.when(reservationRepository.findById(reservationId))
                .thenReturn(Optional.of(super.getReservationById(reservationId)));

        // when
        reservationService.deleteReservation(reservationId);

        // then
        Mockito.verify(reservationRepository, Mockito.times(1)).deleteById(reservationId);
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약이 없는 경우 예외가 발생한다.")
    void deleteReservation_ifNotExist_throwException() {
        // given
        long timeId = 2L;

        // stub
        Mockito.when(reservationRepository.findById(timeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservation(timeId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 예약이 존재하지 않습니다.");
    }
}
