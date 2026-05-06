package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.ReservationAlreadyExistsException;
import roomescape.exception.ReservationNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    private ReservationDao reservationDao;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationDao = mock(ReservationDao.class);
        reservationService = new ReservationService(reservationDao);
    }

    @Test
    void 중복_예약_생성_예외_테스트() {
        when(reservationDao.insertReservation("이든", LocalDate.of(2026, 05, 06), 1L, 1L))
                .thenThrow(new DuplicateKeyException("중복 키 에러"));

        assertThatThrownBy(() -> reservationService.createReservation("이든", LocalDate.of(2026, 05, 06), 1L, 1L))
                .isInstanceOf(ReservationAlreadyExistsException.class);
    }

    @Test
    void 예약_생성_테스트() {
        Long generatedId = 1L;
        Reservation expected = new Reservation(generatedId, "이든", LocalDate.of(2026, 05, 06),
                new ReservationTime(1L, LocalTime.of(10, 0)), 1L);

        when(reservationDao.insertReservation("이든", LocalDate.of(2026, 05, 06), 1L, 1L))
                .thenReturn(generatedId);
        when(reservationDao.findReservationById(generatedId)).thenReturn(expected);

        Reservation actual = reservationService.createReservation("이든", LocalDate.of(2026, 05, 06), 1L, 1L);

        assertThat(actual).isEqualTo(expected);
        verify(reservationDao).insertReservation("이든", LocalDate.of(2026, 05, 06), 1L, 1L);
        verify(reservationDao).findReservationById(generatedId);
    }

    @Test
    void 존재하지_않는_예약_삭제_예외_테스트() {
        when(reservationDao.delete(anyLong())).thenReturn(0);

        assertThatThrownBy(() -> reservationService.deleteReservation(1L))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    void 예약_삭제_성공_테스트() {
        when(reservationDao.delete(1L)).thenReturn(1);

        reservationService.deleteReservation(1L);

        verify(reservationDao).delete(1L);
    }

}