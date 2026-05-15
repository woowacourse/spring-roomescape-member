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
import roomescape.dto.response.AvailableTimeResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.PastReservationTimeException;
import roomescape.exception.ReservationAlreadyExistsException;
import roomescape.exception.ReservationNotFoundException;
import roomescape.exception.ReservationTimeNotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
    void 중복_예약_생성_예외_테스트() {
        LocalDate date = LocalDate.of(2026, 12, 31);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        when(reservationTimeDao.findById(1L)).thenReturn(reservationTime);
        when(reservationDao.insertReservation("이든", date, 1L, 1L))
                .thenThrow(new ReservationAlreadyExistsException("해당 날짜, 시간, 테마에 대한 예약이 이미 존재입니다."));

        assertThatThrownBy(() -> reservationService.createReservation(new ReservationCreateRequest(
                        "이든", date, 1L, 1L
                )))
                .isInstanceOf(ReservationAlreadyExistsException.class);
    }

    @Test
    void 예약_생성_테스트() {
        Long generatedId = 1L;
        LocalDate date = LocalDate.of(2026, 12, 31);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "테마", "설명", "url");
        Reservation expected = new Reservation(generatedId, "이든", date, 1L, 1L);

        when(reservationTimeDao.findById(1L)).thenReturn(time);
        when(reservationDao.insertReservation("이든", date, 1L, 1L)).thenReturn(generatedId);
        when(reservationDao.findReservationById(generatedId)).thenReturn(expected);
        when(themeDao.findById(1L)).thenReturn(theme);

        ReservationResponse actual = reservationService.createReservation(new ReservationCreateRequest(
                "이든", date, 1L, 1L
        ));

        assertThat(actual).isEqualTo(ReservationResponse.from(expected, time, theme));
        verify(reservationDao).insertReservation("이든", date, 1L, 1L);
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

    @Test
    void 존재하지_않는_시간에_대한_예약_생성은_불가능하다() {
        LocalDate date = LocalDate.of(2026, 12, 31);

        when(reservationTimeDao.findById(1L)).thenThrow(new ReservationTimeNotFoundException("해당 시간을 찾을 수 없습니다."));

        assertThatThrownBy(() -> reservationService.createReservation(new ReservationCreateRequest(
                "이든", date, 1L, 1L)))
                .isInstanceOf(ReservationTimeNotFoundException.class)
                .hasMessage("해당 시간을 찾을 수 없습니다.");
    }

    @Test
    void 지나간_날짜와_시간에_대한_예약_생성은_불가능하다() {
        LocalDate date = LocalDate.of(2026, 1, 1);
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));

        when(reservationTimeDao.findById(1L)).thenReturn(reservationTime);

        assertThatThrownBy(() -> reservationService.createReservation(new ReservationCreateRequest(
                "이든", date, 1L, 1L)))
                .isInstanceOf(PastReservationTimeException.class)
                .hasMessage("이전 날짜는 선택하실 수 없습니다.");
    }
    
    @Test
    void 사용자_이름으로_예약_목록_조회_테스트() {
        String name = "브리";
        LocalDate date = LocalDate.of(2026, 12, 31);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "테마", "설명", "url");
        Reservation reservation = new Reservation(1L, name, date, 1L, 1L);

        when(reservationDao.findUserReservations(name)).thenReturn(List.of(reservation));
        when(reservationTimeDao.findById(1L)).thenReturn(time);
        when(themeDao.findById(1L)).thenReturn(theme);

        List<ReservationResponse> actual = reservationService.getUserReservations(name);

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isEqualTo(ReservationResponse.from(reservation, time, theme));
        
        verify(reservationDao).findUserReservations(name);
        verify(reservationTimeDao).findById(1L);
        verify(themeDao).findById(1L);
    }

    @Test
    void 사용자_이름으로_예약_삭제_테스트() {
        String name = "브리";
        LocalDate date = LocalDate.of(2026, 12, 31);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Reservation reservation = new Reservation(1L, name, date, 1L, 1L);

        when(reservationDao.findReservationById(1L)).thenReturn(reservation);
        when(reservationTimeDao.findById(1L)).thenReturn(time);
        when(reservationDao.deleteUserReservation(1L, name)).thenReturn(1);

        reservationService.deleteUserReservation(1L, name);

        verify(reservationDao).deleteUserReservation(1L, name);
    }

    @Test
    void 지나간_날짜와_시간에_대한_예약_삭제는_불가능하다() {
        String name = "브리";
        LocalDate date = LocalDate.of(2026, 1, 1);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(10, 0));
        Reservation reservation = new Reservation(1L, name, date, 1L, 1L);

        when(reservationDao.findReservationById(1L)).thenReturn(reservation);
        when(reservationTimeDao.findById(1L)).thenReturn(time);

        assertThatThrownBy(() -> reservationService.deleteUserReservation(1L, name))
                .isInstanceOf(PastReservationTimeException.class)
                .hasMessage("이전 날짜는 선택하실 수 없습니다.");

    }
}
