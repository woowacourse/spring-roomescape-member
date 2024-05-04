package roomescape.reservation.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.InvalidDateException;
import roomescape.exception.InvalidTimeException;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.time.dao.ReservationTimeDao;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationDao reservationDao;
    @Mock
    private ReservationTimeDao reservationTimeDao;

    @InjectMocks
    private ReservationService reservationService;

    @DisplayName("부적합한 날짜와 시간에 대한 예약 요청 시 예외가 발생한다.")
    @Test
    void save() {
        Long id = 1L;
        ReservationTime reservationTime = new ReservationTime(id, "00:00");
        when(reservationTimeDao.findById(id)).thenReturn(reservationTime);
        when(reservationDao.checkReservationExists(anyString(), anyLong(), anyLong())).thenReturn(true);
        assertAll(
                () -> assertThatThrownBy(() -> reservationService.save(
                        new ReservationRequestDto("hotea", LocalDate.MAX.toString(), id, id)))
                        .isInstanceOf(DuplicateReservationException.class),
                () -> assertThatThrownBy(() -> reservationService.save(
                        new ReservationRequestDto("hotea", LocalDate.now().minusDays(1).toString(), id, id)))
                        .isInstanceOf(InvalidDateException.class),
                () -> assertThatThrownBy(() -> reservationService.save(
                        new ReservationRequestDto("hotea", LocalDate.now().toString(), id, id)))
                        .isInstanceOf(InvalidTimeException.class)
        );
    }
}