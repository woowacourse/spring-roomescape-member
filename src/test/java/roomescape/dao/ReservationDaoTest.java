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

import roomescape.domain.Member;
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
    @Autowired
    private MemberDao memberDao;

    @AfterEach
    void tearDown() {
        reservationDao.deleteAll();
        reservationTimeDao.deleteAll();
        themeDao.deleteAll();
    }

    @Test
    @DisplayName("예약 단권을 조회한다")
    void findById_ShouldGetSinglePersistence() {
        // given
        ReservationTime time = new ReservationTime(LocalTime.of(11, 0));
        Theme theme = new Theme("name", "description", "thumbnail");
        Member member = new Member("memberName", "email", "password");
        ReservationTime savedTime = reservationTimeDao.save(time);
        Theme savedTheme = themeDao.save(theme);
        Member savedMember = memberDao.save(member);
        Reservation savedReservation = reservationDao.save(
                new Reservation(LocalDate.of(2023, FEBRUARY, 1), savedTime, savedTheme, savedMember));

        //when &then
        // then
        Assertions.assertThat(reservationDao.findById(savedReservation.getId()))
                .isPresent();
    }

    @Test
    @DisplayName("예약을 저장한다")
    void save_ShouldSaveReservationPersistence() {
        // given
        ReservationTime time = new ReservationTime(LocalTime.of(11, 0));
        Theme theme = new Theme("name", "description", "thumbnail");
        Member member = new Member("memberName", "email", "password");
        ReservationTime savedTime = reservationTimeDao.save(time);
        Theme savedTheme = themeDao.save(theme);
        Member savedMember = memberDao.save(member);

        // when
        reservationDao.save(
                new Reservation(LocalDate.of(2023, FEBRUARY, 1), savedTime, savedTheme, savedMember));

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
        Member member = new Member("a", "b", "c");
        Reservation savedReservation = reservationDao.save(
                new Reservation(LocalDate.of(2023, FEBRUARY, 1), savedTime, savedTheme, member));

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
        Reservation reservation = new Reservation(LocalDate.of(2023, FEBRUARY, 1), savedTime, savedTheme, null);

        // when & then
        Assertions.assertThatThrownBy(() -> reservationDao.delete(reservation))
                .isInstanceOf(NotFoundReservationException.class);
    }


}
