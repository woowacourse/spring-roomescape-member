package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.PastReservationTimeException;
import roomescape.exception.ReservationNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReservationServiceTest {

    private ReservationDao reservationDao;
    private ReservationTimeDao reservationTimeDao;
    private ThemeDao themeDao;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationDao = mock(ReservationDao.class);
        reservationTimeDao = mock(ReservationTimeDao.class);
        themeDao = mock(ThemeDao.class);
        reservationService = new ReservationService(reservationDao, reservationTimeDao, themeDao);
    }

    @Test
    void 예약_생성_테스트() {
        long generatedId = 1L;
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationTime time = ReservationTime.from(1L, LocalTime.of(10, 0));
        Theme theme = Theme.from(1L, "테마", "설명", "url");
        ReservationCreateRequest request = new ReservationCreateRequest("이든", date, 1L, 1L);
        Reservation savedReservation = Reservation.from(generatedId, "이든", date, 1L, 1L);

        when(reservationTimeDao.findById(1L)).thenReturn(time);
        when(themeDao.findById(1L)).thenReturn(theme);
        when(reservationDao.insertReservation(any(Reservation.class))).thenReturn(generatedId);
        when(reservationDao.findReservationById(generatedId)).thenReturn(savedReservation);

        ReservationResponse actual = reservationService.createReservation(request);

        assertThat(actual.id()).isEqualTo(generatedId);
        assertThat(actual.name()).isEqualTo("이든");
        assertThat(actual.theme().name()).isEqualTo("테마");
    }

    @Test
    void 지나간_날짜와_시간에_대한_예약_생성은_불가능하다() {
        LocalDate date = LocalDate.of(2020, 1, 1);
        ReservationTime time = ReservationTime.from(1L, LocalTime.of(10, 0));
        ReservationCreateRequest request = new ReservationCreateRequest("이든", date, 1L, 1L);

        when(reservationTimeDao.findById(1L)).thenReturn(time);

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(PastReservationTimeException.class);
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_예외가_발생한다() {
        when(reservationDao.delete(anyLong())).thenReturn(0);

        assertThatThrownBy(() -> reservationService.deleteReservation(1L))
                .isInstanceOf(ReservationNotFoundException.class);
    }
}
