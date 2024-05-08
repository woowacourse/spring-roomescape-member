package roomescape.dao;

import static java.time.Month.FEBRUARY;

import java.time.LocalDate;
import java.time.LocalTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.reservation.NotFoundReservationException;

@SpringBootTest
class ReservationDaoTest {
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ReservationTimeDao reservationTimeDao;
    @Autowired
    private ThemeDao themeDao;

    @AfterEach
    void tearDown() {
        reservationDao.deleteAll();
        reservationTimeDao.deleteAll();
        themeDao.deleteAll();
    }

    @Test
    @DisplayName("예약을 저장한다")
    void save_ShouldSaveReservationPersistence() {
        // given
        ReservationTime time = new ReservationTime(LocalTime.of(11, 0));
        Theme theme = new Theme("name", "description", "thumbnail");
        ReservationTime savedTime = reservationTimeDao.save(time);
        Theme savedTheme = themeDao.save(theme);

        // when
        reservationDao.save(
                new Reservation("name", LocalDate.of(2023, FEBRUARY, 1), savedTime, savedTheme));

        // then
        Assertions.assertThat(reservationDao.findAll())
                .hasSize(1);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void delete_ShouldRemovePersistence() {
        // given
        ReservationTime time = new ReservationTime(LocalTime.of(11, 0));
        Theme theme = new Theme("name", "description", "thumbnail");
        ReservationTime savedTime = reservationTimeDao.save(time);
        Theme savedTheme = themeDao.save(theme);
        Reservation savedReservation = reservationDao.save(
                new Reservation("name", LocalDate.of(2023, FEBRUARY, 1), savedTime, savedTheme));

        // when
        reservationDao.delete(savedReservation);

        // then
        Assertions.assertThat(reservationDao.findById(savedReservation.getId()))
                .isEmpty();
    }


    @Test
    @DisplayName("없는 예약에 대한 삭제 요청시 예외를 발생시킨다.")
    void remove_ShouldThrowException_WhenReservationDoesNotExist() {
        ReservationTime time = new ReservationTime(LocalTime.of(11, 0));
        Theme theme = new Theme("name", "description", "thumbnail");
        ReservationTime savedTime = reservationTimeDao.save(time);
        Theme savedTheme = themeDao.save(theme);
        Reservation reservation = new Reservation("name", LocalDate.of(2023, FEBRUARY, 1), savedTime, savedTheme);

        // when & then
        Assertions.assertThatThrownBy(() -> reservationDao.delete(reservation))
                .isInstanceOf(NotFoundReservationException.class);
    }


}
