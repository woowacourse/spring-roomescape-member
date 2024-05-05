package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.service.dto.ReservationTimeRequest;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @DisplayName("예약 시간을 조회할 땐 findAllReservationTimes이 호출되어야 한다.")
    @Test
    void find_all_reservation_times() {
        ReservationTime time1 = new ReservationTime(1L, "11:30");
        ReservationTime time2 = new ReservationTime(2L, "23:59");
        List<ReservationTime> reservationTimes = List.of(time1, time2);
        given(reservationTimeRepository.findAllReservationTimes()).willReturn(reservationTimes);

        reservationTimeService.findAllReservationTimes();
        verify(reservationTimeRepository, times(1)).findAllReservationTimes();
    }

    @DisplayName("예약 시간을 정상적으로 저장할 땐 insertReservationTime이 호출되어야 한다.")
    @Test
    void create_reservation_time_test() {
        ReservationTimeRequest requestDto = new ReservationTimeRequest("11:30");
        ReservationTime reservationTime = new ReservationTime(1L, "11:30");
        given(reservationTimeRepository.isExistTimeOf(requestDto.getStartAt())).willReturn(false);
        given(reservationTimeRepository.insertReservationTime(requestDto.toReservationTime())).willReturn(
                reservationTime);

        reservationTimeService.createReservationTime(requestDto);
        verify(reservationTimeRepository, times(1)).insertReservationTime(requestDto.toReservationTime());
    }

    @DisplayName("중복된 시간을 생성하려고 하면 예외가 발생한다.")
    @Test
    void throw_exception_when_duplicate_time_create() {
        ReservationTimeRequest requestDto = new ReservationTimeRequest("11:30");
        given(reservationTimeRepository.isExistTimeOf(requestDto.getStartAt())).willReturn(true);

        assertThatThrownBy(() -> reservationTimeService.createReservationTime(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 시간을 입력할 수 없습니다.");
    }

    @DisplayName("예약이 존재하는 시간을 삭제하려고 하면 예외가 발생한다.")
    @Test
    void throw_exception_when_exist_reservation_delete() {
        long timeId = 1L;
        given(reservationTimeRepository.isExistTimeOf(timeId)).willReturn(true);
        given(reservationRepository.hasReservationOfTimeId(timeId)).willReturn(true);

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(timeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간에 예약이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("존재하지 않는 아이디를 삭제하려고 하면 예외가 발생한다.")
    @Test
    void throw_exception_when_not_exist_id_delete() {
        long timeId = 1L;
        given(reservationTimeRepository.isExistTimeOf(timeId)).willReturn(false);

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(timeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 아이디입니다.");
    }

    @DisplayName("예약이 존재하지 않는 시간을 삭제할 수 있다.")
    @Test
    void delete_reservation_time() {
        long timeId = 1L;
        given(reservationTimeRepository.isExistTimeOf(timeId)).willReturn(true);
        given(reservationRepository.hasReservationOfTimeId(timeId)).willReturn(false);

        reservationTimeService.deleteReservationTime(timeId);
        verify(reservationTimeRepository, times(1)).deleteReservationTimeById(timeId);
    }
}
