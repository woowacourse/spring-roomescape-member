package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.ReservationAlreadyExistsException;
import roomescape.exception.ReservationNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {

    private ReservationDao reservationDao;
    private ReservationTimeDao reservationTimeDao;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationDao = mock(ReservationDao.class);
        reservationTimeDao = mock(ReservationTimeDao.class);
        reservationService = new ReservationService(reservationDao, reservationTimeDao);
    }

    @Test
    void 중복_예약_생성_예외_테스트() {
        when(reservationDao.insertReservation("이든", LocalDate.of(2026, 05, 06), 1L, 1L))
                .thenThrow(new DuplicateKeyException("중복 키 에러"));

        assertThatThrownBy(() -> reservationService.createReservation(new ReservationCreateRequest(
                        "이든", LocalDate.of(2026, 05, 06), 1L, 1L
                )))
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

        ReservationResponse actual = reservationService.createReservation(new ReservationCreateRequest(
                "이든", LocalDate.of(2026, 05, 06), 1L, 1L
        ));

        assertThat(actual).isEqualTo(ReservationResponse.from(expected));
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

    @Test
    void 예약_가능_시간_조회_테스트() {
        LocalDate date = LocalDate.of(2026, 5, 6);
        Long themeId = 1L;

        ReservationTime time1 = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime time2 = new ReservationTime(2L, LocalTime.of(11, 0));
        ReservationTime time3 = new ReservationTime(3L, LocalTime.of(12, 0));

        when(reservationTimeDao.findAllReservationTimes()).thenReturn(List.of(time1, time2, time3));
        when(reservationDao.findReservationTimeIds(date, themeId)).thenReturn(List.of(1L, 3L)); // 10시, 12시는 예약됨

        List<AvailableTimeResponse> actual = reservationService.getAvailableTimes(date, themeId);

        assertThat(actual).hasSize(3);

        assertThat(actual.get(0).id()).isEqualTo(1L);
        assertThat(actual.get(0).available()).isFalse();

        assertThat(actual.get(1).id()).isEqualTo(2L);
        assertThat(actual.get(1).available()).isTrue();

        assertThat(actual.get(2).id()).isEqualTo(3L);
        assertThat(actual.get(2).available()).isFalse();
    }
}
