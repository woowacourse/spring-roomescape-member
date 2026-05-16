package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.reservationtime.DuplicateReservationTimeException;
import roomescape.global.exception.reservationtime.ReservationTimeInUseException;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.dto.reservationtime.CreateReservationTimeCommand;

class ReservationTimeServiceTest {

    private ReservationTimeRepository reservationTimeRepository;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        reservationTimeRepository = mock(ReservationTimeRepository.class);
        ThemeRepository themeRepository = mock(ThemeRepository.class);
        reservationTimeService = new ReservationTimeService(reservationTimeRepository, themeRepository);
    }

    @Test
    void 동일한_시작_시간을_중복_등록할_수_없다() {
        LocalTime startAt = LocalTime.of(10, 0);
        when(reservationTimeRepository.existsByStartAt(startAt)).thenReturn(true);

        assertThatThrownBy(() -> reservationTimeService.createReservationTime(
                new CreateReservationTimeCommand(startAt)))
                .isInstanceOf(DuplicateReservationTimeException.class)
                .hasMessage("이미 등록된 예약 시간입니다.");
    }

    @Test
    void 예약이_존재하는_시간은_삭제할_수_없다() {
        when(reservationTimeRepository.existsReservationByTimeId(1L)).thenReturn(true);

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
                .isInstanceOf(ReservationTimeInUseException.class)
                .hasMessage("예약이 존재하는 시간은 삭제할 수 없습니다.");
    }
}
