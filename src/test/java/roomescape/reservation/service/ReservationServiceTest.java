package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.RoomescapeException;
import roomescape.reservation.Reservation;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.dto.ReservationChangeRequest;
import roomescape.time.ReservationTime;
import roomescape.time.dao.TimeDao;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private TimeDao timeDao;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void 이름으로_예약_조회_성공() {
        ReservationTime time = new ReservationTime(22L, LocalTime.now().plusMinutes(3));
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation("초록", 1L, LocalDate.now(), time));
        reservations.add(new Reservation("초록", 1L, LocalDate.now(), time));
        when(reservationDao.selectByName(any(String.class))).thenReturn(reservations);

        assertThat(reservationService.findByName("초록").size()).isEqualTo(2);
    }

    @Test
    void 지난_날짜및시간_예약_하는_경우_예외발생() {
        ReservationTime mockTime = new ReservationTime(17L, LocalTime.now().minusMinutes(10));
        when(timeDao.selectById(anyLong())).thenReturn(mockTime);

        assertThatThrownBy(() -> reservationService.add("브라운", 1L, LocalDate.of(2026, 5, 10), 1L))
                .isInstanceOf(RoomescapeException.class)
                .hasMessage(ErrorCode.PAST_RESERVATION.getMessage());

        assertThatThrownBy(() -> reservationService.add("브라운", 1L, LocalDate.now(), mockTime.getId()))
                .isInstanceOf(RoomescapeException.class)
                .hasMessage(ErrorCode.PAST_RESERVATION.getMessage());
    }

    @Test
    void 이미_예약이_존재하는_경우_예외발생() {
        ReservationTime mockTime = new ReservationTime(1L, LocalTime.parse("10:00"));
        when(timeDao.selectById(anyLong())).thenReturn(mockTime);

        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation = new Reservation("초록", 1L, LocalDate.of(2026, 5, 20), mockTime);
        reservations.add(reservation);
        when(reservationDao.selectByThemeIdAndDate(anyLong(), any(LocalDate.class))).thenReturn(reservations);

        assertThatThrownBy(() -> reservationService.add("브라운", 1L, LocalDate.of(2026, 5, 20), mockTime.getId()))
                .isInstanceOf(RoomescapeException.class)
                .hasMessage(ErrorCode.RESERVATION_ALREADY_EXISTS.getMessage());
    }

    @Test
    void 본인_예약_변경_성공() {
        Long reservationId = 1L;
        String name = "로치";
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2026, 5, 20);
        Long timeId = 2L;
        ReservationTime time = new ReservationTime(timeId, LocalTime.of(12, 0));

        Reservation originReservation = new Reservation(
                reservationId,
                name,
                themeId,
                LocalDate.of(2026, 5, 23),
                new ReservationTime(3L, LocalTime.of(10, 0))
        );

        Reservation changedReservation = new Reservation(
                reservationId,
                name,
                themeId,
                date,
                time
        );

        given(reservationDao.selectById(reservationId))
                .willReturn(Optional.of(originReservation));
        given(timeDao.selectById(timeId))
                .willReturn(time);
        given(reservationDao.selectByThemeIdAndDate(themeId, date))
                .willReturn(List.of());
        given(reservationDao.updateDateTimeById(reservationId, date, timeId))
                .willReturn(Optional.of(changedReservation));

        Reservation reservation = reservationService.modifyDateTimeByName(
                reservationId,
                name,
                themeId,
                date,
                timeId
        );

        assertThat(reservation.getId()).isEqualTo(reservationId);
        assertThat(reservation.getName()).isEqualTo(name);
        assertThat(reservation.getDate()).isEqualTo(date);
        assertThat(reservation.getTime().getId()).isEqualTo(timeId);
    }

    @Test
    void 다른_사람의_예약은_변경할_수_없다() {
        Long reservationId = 1L;
        Reservation reservation = new Reservation(
                reservationId,
                "로치",
                1L,
                LocalDate.of(2026, 5, 23),
                new ReservationTime(3L, LocalTime.of(12, 0))
        );

        ReservationChangeRequest request = new ReservationChangeRequest(
                "브라운",
                1L,
                LocalDate.of(2026, 5, 20),
                2L
        );

        given(reservationDao.selectById(reservationId))
                .willReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.modifyDateTimeByName(
                reservationId,
                request.name(),
                request.themeId(),
                request.date(),
                request.timeId()))
                .isInstanceOf(RoomescapeException.class)
                .hasMessageContaining(ErrorCode.CANNOT_MODIFY_OTHER_RESERVATION.getMessage());
    }

    @Test
    void 존재하지_않는_예약은_변경할_수_없다() {
        Long notFoundId = 999L;

        ReservationChangeRequest request = new ReservationChangeRequest(
                "로치",
                1L,
                LocalDate.of(2026, 5, 20),
                2L
        );

        assertThatThrownBy(() -> reservationService.modifyDateTimeByName(
                notFoundId,
                request.name(),
                request.themeId(),
                request.date(),
                request.timeId()))
                .isInstanceOf(RoomescapeException.class)
                .hasMessageContaining(ErrorCode.RESERVATION_NOT_FOUND.getMessage());
    }

    @Test
    void 본인_예약_취소_성공() {
        Long reservationId = 1L;
        String name = "로치";
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2026, 5, 20);
        Long timeId = 2L;
        ReservationTime time = new ReservationTime(timeId, LocalTime.of(12, 0));

        Reservation originReservation = new Reservation(
                reservationId,
                name,
                themeId,
                LocalDate.of(2026, 5, 23),
                new ReservationTime(3L, LocalTime.of(10, 0))
        );

        given(reservationDao.selectById(reservationId))
                .willReturn(Optional.of(originReservation));

        reservationService.deleteIdByName(reservationId, name);

        verify(reservationDao, times(1)).deleteById(reservationId);
    }

    @Test
    void 다른_사람의_예약은_취소할_수_없다() {
        Long reservationId = 1L;
        Reservation reservation = new Reservation(
                reservationId,
                "로치",
                1L,
                LocalDate.of(2026, 5, 23),
                new ReservationTime(3L, LocalTime.of(12, 0))
        );

        ReservationChangeRequest request = new ReservationChangeRequest(
                "브라운",
                1L,
                LocalDate.of(2026, 5, 20),
                2L
        );

        given(reservationDao.selectById(reservationId))
                .willReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.deleteIdByName(reservationId, request.name()))
                .isInstanceOf(RoomescapeException.class)
                .hasMessageContaining(ErrorCode.CANNOT_DELETE_OTHER_RESERVATION.getMessage());
    }

    @Test
    void 존재하지_않는_예약은_취소할_수_없다() {
        Long notFoundId = 999L;

        ReservationChangeRequest request = new ReservationChangeRequest(
                "로치",
                1L,
                LocalDate.of(2026, 5, 20),
                2L
        );

        assertThatThrownBy(() -> reservationService.deleteIdByName(notFoundId, request.name()))
                .isInstanceOf(RoomescapeException.class)
                .hasMessageContaining(ErrorCode.RESERVATION_NOT_FOUND.getMessage());
    }
}
