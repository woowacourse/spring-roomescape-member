package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.TestConfig;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Person;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.BookedReservationTimeResponseDto;
import roomescape.dto.ReservationRequestDto;
import roomescape.exception.InvalidReservationException;
import roomescape.service.nowdate.CurrentDateTime;

public class ReservationServiceTest {

    private ThemeDao themeDao = Mockito.mock(ThemeDao.class);
    private ReservationTimeDao reservationTimeDao = Mockito.mock(ReservationTimeDao.class);
    private ReservationDao reservationDao = Mockito.mock(ReservationDao.class);
    private CurrentDateTime currentDateTime = new TestConfig().currentDate();

    private ReservationService reservationService = new ReservationService(
        reservationDao, reservationTimeDao, themeDao, currentDateTime
    );

    @DisplayName("삭제할 예약 id가 주어졌을 때, 존재하지 않는다면 예외가 발생해야 한다.")
    @Test
    void given_delete_invalid_reservation_id_then_throw_exception() {
        when(reservationDao.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> reservationService.deleteReservation(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("삭제할 예약 id가 주어졌을 때, 존재하는 값이라면, 삭제 되어야 한다.")
    @Test
    void given_delete_valid_reservation_id_then_throw_exception() {
        //given
        Reservation reservation = new Reservation(
            new Person("jenson"),
            new ReservationDate(LocalDate.of(2025, 5, 10)),
            new ReservationTime(1L, LocalTime.of(10, 0)),
            new Theme(1L, "공포", "공포테마입니다", "http://aaa")
        );
        long savedReservationId = reservationDao.saveReservation(reservation);
        when(reservationDao.findById(savedReservationId))
            .thenReturn(Optional.of(reservation));

        //when
        reservationService.deleteReservation(savedReservationId);

        //then
        assertThat(reservationService.getAllReservations().size()).isEqualTo(0);
    }

    @DisplayName("이미 존재하는 날짜와 시간에 예약하려고 하면, 예외가 발생해야 한다.")
    @Test
    void already_exist_date_time_save_reservation_then_throw_exception() {
        ReservationDate reservationDate = new ReservationDate(LocalDate.of(2025, 5, 5));
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "공포", "공포테마입니다", "http://aaa");
        when(reservationTimeDao.findById(1L)).thenReturn(Optional.of(reservationTime));
        when(themeDao.findById(1L)).thenReturn(Optional.of(theme));
        when(reservationDao.findByDateAndTime(reservationDate, 1L)).thenReturn(1);
        ReservationRequestDto reservationRequestDto1 = new ReservationRequestDto(
            "jenson",
            "2025-05-05",
            1L,
            1L
        );
        assertThatThrownBy(() -> reservationService.saveReservation(reservationRequestDto1))
            .isInstanceOf(InvalidReservationException.class)
            .hasMessage("중복된 날짜와 시간을 예약할 수 없습니다.");
    }

    // TODO : 예약 여부를 포함한 예약 시간을 가져올 수 있어야 한다.
    @DisplayName("예약 여부를 포함한 예약 시간을 가져올 수 있어야 한다")
    @Test
    void get_all_reservation_contains_book_info() {
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTime reservationTime2 = new ReservationTime(2L, LocalTime.of(11, 0));
        ReservationTime reservationTime3 = new ReservationTime(3L, LocalTime.of(12, 0));

        List<ReservationTime> reservationTimes = List.of(reservationTime1, reservationTime2,
            reservationTime3);
        when(reservationTimeDao.findAllReservationTimes()).thenReturn(reservationTimes);
        List<BookedReservationTimeResponseDto> allBookedReservationTimes =
            reservationService.getAllBookedReservationTimes("2025-05-02", 1L);

        assertThat(allBookedReservationTimes.get(0).alreadyBooked()).isFalse();
        assertThat(allBookedReservationTimes.get(1).alreadyBooked()).isFalse();
        assertThat(allBookedReservationTimes.get(2).alreadyBooked()).isFalse();
    }

    @DisplayName("예약이 존재한 시간이라면 true를 반환해야 한다.")
    @Test
    void get_all_reservation_contains_book_info_already_exist_case() {
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.of(10, 0));

        String date = "2025-05-02";
        List<ReservationTime> reservationTimes = List.of(reservationTime1);
        when(reservationTimeDao.findAllReservationTimes()).thenReturn(reservationTimes);
        when(reservationDao.calculateAlreadyExistReservationBy(date, 1L, 1L)).thenReturn(1);

        List<BookedReservationTimeResponseDto> allBookedReservationTimes =
            reservationService.getAllBookedReservationTimes("2025-05-02", 1L);
        assertThat(allBookedReservationTimes.get(0).alreadyBooked()).isTrue();
    }
}
