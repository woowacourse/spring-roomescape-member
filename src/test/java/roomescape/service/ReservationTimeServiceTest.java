package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.BadRequestException;
import roomescape.exception.DuplicatedException;
import roomescape.exception.NotFoundException;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.dao.ReservationDao;
import roomescape.repository.dao.ReservationTimeDao;
import roomescape.repository.dto.ReservationSavedDto;
import roomescape.service.dto.ReservationTimeDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ReservationTimeServiceTest {

    private ReservationTimeRepository reservationTimeRepository;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() { // 테케 의존성 분리하기
        ReservationTimeDao reservationTimeDao = new FakeReservationTimeDao(new ArrayList<>(List.of(
                new ReservationTime(1, LocalTime.of(10, 0)),
                new ReservationTime(2, LocalTime.of(11, 0)))));
        ReservationDao reservationDao = new FakeReservationDao(new ArrayList<>(List.of(
                new ReservationSavedDto(1, "브라운", LocalDate.of(2030, 8, 5), 2L, 1L),
                new ReservationSavedDto(1, "리사", LocalDate.of(2030, 8, 1), 2L, 2L))));
        reservationTimeRepository = new ReservationTimeRepository(reservationDao, reservationTimeDao);
        reservationTimeService = new ReservationTimeService(reservationTimeRepository);
    }

    @DisplayName("모든 예약 시간을 반환한다")
    @Test
    void should_return_all_reservation_times() {
        List<ReservationTime> reservationTimes = reservationTimeService.findAllReservationTimes();
        assertThat(reservationTimes).hasSize(2);
    }

    @DisplayName("아이디에 해당하는 예약 시간을 반환한다.")
    @Test
    void should_get_reservation_time() {
        ReservationTime reservationTime = reservationTimeService.findReservationTime(2);
        assertThat(reservationTime.getStartAt()).isEqualTo(LocalTime.of(11, 0));
    }

    @DisplayName("예약 시간을 추가한다")
    @Test
    void should_add_reservation_times() {
        reservationTimeService.saveReservationTime(new ReservationTimeDto(LocalTime.of(12, 0)));
        List<ReservationTime> allReservationTimes = reservationTimeService.findAllReservationTimes();
        assertThat(allReservationTimes).hasSize(3);
    }

    @DisplayName("예약 시간을 삭제한다")
    @Test
    void should_remove_reservation_times() {
        reservationTimeService.deleteReservationTime(1);
        List<ReservationTime> allReservationTimes = reservationTimeService.findAllReservationTimes();
        assertThat(allReservationTimes).hasSize(1);
    }

    @DisplayName("존재하지 않는 시간이면 예외를 발생시킨다.")
    @Test
    void should_throw_exception_when_not_exist_id() {
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(10000000))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] 존재하지 않는 시간입니다.");
    }

    @DisplayName("존재하는 시간이면 예외가 발생하지 않는다.")
    @Test
    void should_not_throw_exception_when_exist_id() {
        assertThatCode(() -> reservationTimeService.deleteReservationTime(1))
                .doesNotThrowAnyException();
    }

    @DisplayName("특정 시간에 대핸 예약이 존재하는데, 그 시간을 삭제하려 할 때 예외가 발생한다.")
    @Test
    void should_throw_exception_when_exist_reservation_using_time() {
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(2))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 해당 시간을 사용하고 있는 예약이 있습니다.");
    }

    @DisplayName("존재하는 시간을 추가하려 할 때 예외가 발생한다.")
    @Test
    void should_throw_exception_when_add_exist_time() {
        assertThatThrownBy(() -> reservationTimeService.saveReservationTime(new ReservationTimeDto(LocalTime.of(10, 0))))
                .isInstanceOf(DuplicatedException.class)
                .hasMessage("[ERROR] 중복되는 시간은 추가할 수 없습니다.");
    }
}
