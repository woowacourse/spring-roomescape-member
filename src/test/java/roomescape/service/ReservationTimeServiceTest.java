package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.BookedReservationTimeResponseDto;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.exception.InvalidReservationException;

@Sql(scripts = {"/test-schema.sql"})
@SpringBootTest
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @MockitoBean
    private ReservationDao reservationDao;

    @MockitoBean
    private ReservationTimeDao reservationTimeDao;

    @DisplayName("예약 객체가 주어졌을 때, ID를 설정할 수 있어야 한다.")
    @Test
    void given_reservation_then_set_id() {
        ReservationTimeRequestDto reservationTimeRequestDto = new ReservationTimeRequestDto(
            "10:00");
        ReservationTimeResponseDto reservationTimeResponseDto = reservationTimeService.saveReservationTime(
            reservationTimeRequestDto);
        assertThat(reservationTimeResponseDto.id()).isNotNull();
    }

    @DisplayName("다른 예약에서 예약 시간을 이미 사용하고 있다면, 삭제할 수 없다.")
    @Test
    void delete_invalid_time_id_then_throw_exception() {
        when(reservationDao.findByTimeId(1L)).thenReturn(1);
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
            .isInstanceOf(InvalidReservationException.class);
    }

    @DisplayName("다른 예약에서 예약 시간을 사용하고 있지 않다면, 삭제할 수 있어야 한다..")
    @Test
    void delete_valid_time_id_then_throw_exception() {
        when(reservationDao.findByTimeId(1L)).thenReturn(0);
        assertThatCode(() -> reservationTimeService.deleteReservationTime(1L))
            .doesNotThrowAnyException();
    }

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
            reservationTimeService.getTimesContainsReservationInfoBy("2025-05-02", 1L);

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
            reservationTimeService.getTimesContainsReservationInfoBy("2025-05-02", 1L);
        assertThat(allBookedReservationTimes.get(0).alreadyBooked()).isTrue();
    }
}
