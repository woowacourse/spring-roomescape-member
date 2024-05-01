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
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.service.dto.ReservationTimeRequestDto;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @DisplayName("모든 예약 시간 정보 조회 및 의존 객체 상호작용 테스트")
    @Test
    void find_all_reservation_times() {
        ReservationTime time1 = new ReservationTime(1L, "11:30");
        ReservationTime time2 = new ReservationTime(2L, "23:59");
        List<ReservationTime> reservationTimes = List.of(time1, time2);
        given(reservationTimeRepository.findAllReservationTimes()).willReturn(reservationTimes);

        reservationTimeService.findAllReservationTimes();
        verify(reservationTimeRepository, times(1)).findAllReservationTimes();
    }

    @DisplayName("예약 시간 저장 및 의존 객체 상호작용 테스트")
    @Test
    void create_reservation_time_test() {
        ReservationTimeRequestDto requestDto = new ReservationTimeRequestDto("11:30");
        ReservationTime reservationTime = new ReservationTime(1L, "11:30");
        given(reservationTimeRepository.insertReservationTime(requestDto.toReservationTime())).willReturn(
                reservationTime);

        reservationTimeService.createReservationTime(requestDto);
        verify(reservationTimeRepository, times(1)).insertReservationTime(requestDto.toReservationTime());
    }

    @DisplayName("중복된 시간을 생성하려고 하면 예외가 발생한다.")
    @Test
    void throw_exception_when_duplicate_time_create() {
        ReservationTimeRequestDto requestDto = new ReservationTimeRequestDto("11:30");
        given(reservationTimeRepository.isExistTimeOf(requestDto.getStartAt())).willReturn(true);

        assertThatThrownBy(() -> reservationTimeService.createReservationTime(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 시간을 입력할 수 없습니다.");
    }
}
