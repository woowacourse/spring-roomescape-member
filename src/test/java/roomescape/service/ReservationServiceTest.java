package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.service.dto.ReservationRequest;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    private ReservationService reservationService;

    private final ReservationTime time = new ReservationTime(1L, "15:30");
    private final LocalDate validDate = LocalDate.now().plusDays(10);
    private final ReservationRequest requestDto = new ReservationRequest("재즈", 1L, validDate.toString(), 1L);

    @DisplayName("예약 리스트를 조회할 땐 findAllReservations이 호출되어야 한다")
    @Test
    void find_all_reservations_test() {
        Reservation reservation1 = new Reservation(
                1L,
                1L, "안돌", "admin", "email", "password",
                1L, "테마이름", "테마내용", "테마썸네일",
                "2023-09-08",
                1L, "15:30");
        Reservation reservation2 = new Reservation(
                1L,
                2L, "재즈", "user", "email", "password",
                1L, "테마이름", "테마내용", "테마썸네일",
                "2024-04-22",
                2L, "17:30");
        List<Reservation> reservations = List.of(reservation1, reservation2);
        given(reservationRepository.findAllReservations()).willReturn(reservations);

        reservationService.findAllReservations();
        verify(reservationRepository, times(1)).findAllReservations();
    }

    @DisplayName("예약을 정상적으로 저장할 땐 insertReservation이 호출되어야 한다.")
    @Test
    void create_reservation_test() {
        given(reservationTimeRepository.isExistTimeOf(requestDto.getTimeId())).willReturn(true);
        given(reservationRepository.hasSameReservationForThemeAtDateTime(requestDto.toReservation())).willReturn(false);
        given(reservationTimeRepository.findReservationTimeById(requestDto.getTimeId())).willReturn(time);

        Reservation reservation = new Reservation(
                1L,
                1L, "안돌", "admin", "email", "password",
                1L, "테마이름", "테마내용", "테마썸네일",
                "2023-09-08",
                1L, "15:30");
        given(reservationRepository.insertReservation(requestDto.toReservation())).willReturn(reservation);

        reservationService.createReservation(requestDto);
        verify(reservationRepository, times(1)).insertReservation(requestDto.toReservation());
    }

    @DisplayName("존재하지 않는 시간의 예약을 생성하려고 하면 예외가 발생한다.")
    @Test
    void throw_exception_when_not_exist_time_id_create() {
        given(reservationTimeRepository.isExistTimeOf(requestDto.getTimeId())).willReturn(false);

        assertThatThrownBy(() -> reservationService.createReservation(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 하려는 시간이 저장되어 있지 않습니다.");

        verify(reservationRepository, never()).insertReservation(requestDto.toReservation());
    }

    @DisplayName("지나간 날짜와 시간에 대한 예약을 생성하려고 하면 예외가 발생한다.")
    @Test
    void throw_exception_when_past_datetime_create() {
        LocalDate pastDate = LocalDate.now().minusDays(10);
        ReservationRequest invalidDateRequest = new ReservationRequest("재즈", 1L, pastDate.toString(), 1L);
        given(reservationTimeRepository.isExistTimeOf(invalidDateRequest.getTimeId())).willReturn(true);
        given(reservationTimeRepository.findReservationTimeById(requestDto.getTimeId())).willReturn(time);

        assertThatThrownBy(() -> reservationService.createReservation(invalidDateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지나간 날짜와 시간에 대한 예약은 불가능합니다.");

        verify(reservationRepository, never()).insertReservation(requestDto.toReservation());
    }

    @DisplayName("이미 예약된 날짜와 시간에 예약을 생성하려고 하면 예외가 발생한다.")
    @Test
    void throw_exception_when_duplicate_datetime_create() {
        given(reservationTimeRepository.isExistTimeOf(requestDto.getTimeId())).willReturn(true);
        given(reservationTimeRepository.findReservationTimeById(requestDto.getTimeId())).willReturn(time);
        given(reservationRepository.hasSameReservationForThemeAtDateTime(requestDto.toReservation())).willReturn(true);

        assertThatThrownBy(() -> reservationService.createReservation(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테마는 같은 시간에 이미 예약이 존재합니다.");

        verify(reservationRepository, never()).insertReservation(requestDto.toReservation());
    }

    @DisplayName("존재하지 않는 아이디를 삭제하려고 하면 예외가 발생한다.")
    @Test
    void throw_exception_when_not_exist_id_delete() {
        long id = 1L;
        given(reservationRepository.isExistReservationOf(id)).willReturn(false);

        assertThatThrownBy(() -> reservationService.deleteReservation(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 아이디입니다.");

        verify(reservationRepository, never()).deleteReservationById(requestDto.getTimeId());
    }
}
