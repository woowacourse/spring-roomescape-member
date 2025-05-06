package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.FakeReservationDaoImpl;
import roomescape.dao.FakeReservationTimeDaoImpl;
import roomescape.dao.FakeThemeDaoImpl;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Person;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.InvalidReservationException;
import roomescape.repository.impl.ReservationTimeRepositoryImpl;

public class ReservationTimeRepositoryTest {

    private ReservationTimeDao reservationTimeDao;
    private ReservationDao reservationDao;
    private ThemeDao themeDao;
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void init() {
        reservationTimeDao = new FakeReservationTimeDaoImpl();
        reservationDao = new FakeReservationDaoImpl();
        themeDao = new FakeThemeDaoImpl();
        reservationTimeRepository = new ReservationTimeRepositoryImpl(
            reservationTimeDao, reservationDao);
    }

    @DisplayName("존재하지 않는 예약 시간을 삭제하려고 할 경우, 예외가 발생해야 한다.")
    @Test
    void delete_not_exist_reservation_id_then_throw_exception() {
        assertThatThrownBy(() -> reservationTimeRepository.deleteReservationTime(10000L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 예약 시간을 삭제하려고 할 경우, 성공해야 한다.")
    @Test
    void exist_reservation_id_delete_then_success() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        long savedReservationTimeId = reservationTimeDao.saveReservationTime(reservationTime);
        reservationTime.setId(savedReservationTimeId);
        assertThatCode(
            () -> reservationTimeRepository.deleteReservationTime(savedReservationTimeId))
            .doesNotThrowAnyException();
    }

    @DisplayName("다른 예약에서 사용중인 예약 시간을 사용하려고 할 경우, 예외가 발생해야 한다.")
    @Test
    void delete_already_use_other_reservation_time_id_then_throw_exception() {

        //given
        Theme theme = new Theme("공포", "공포테마입니다", "http://aaa");
        long savedThemeId = themeDao.saveTheme(theme);
        theme.setId(savedThemeId);

        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        long savedReservationTimeId = reservationTimeDao.saveReservationTime(reservationTime);
        reservationTime.setId(savedReservationTimeId);

        Reservation reservation = new Reservation(
            new Person("jenson"),
            new ReservationDate(LocalDate.of(2025, 5, 5)),
            reservationTime,
            theme
        );

        long savedId = reservationDao.saveReservation(reservation);
        reservation.setId(savedId);

        //when
        assertThatThrownBy(
            () -> reservationTimeRepository.deleteReservationTime(savedReservationTimeId))
            .isInstanceOf(InvalidReservationException.class)
            .hasMessage("이미 예약된 예약 시간을 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 id를 조회하려고 할 경우, 예외가 발생해야 한다.")
    void not_exist_reservation_id_then_throw_exception() {
        assertThatThrownBy(() -> reservationTimeRepository.findById(999999L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하는 예약 시간 id를 조회하려고 할 경우, 성공해야 한다.")
    void exist_reservation_id_get_then_success() {
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 0));
        long savedReservationTimeId = reservationTimeDao.saveReservationTime(reservationTime);
        reservationTime.setId(savedReservationTimeId);

        assertThatCode(
            () -> reservationTimeRepository.findById(savedReservationTimeId))
            .doesNotThrowAnyException();
    }
}
