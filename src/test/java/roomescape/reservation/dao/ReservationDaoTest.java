package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import roomescape.auth.dao.MemberDao;
import roomescape.auth.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.time.dao.ReservationTimeDao;
import roomescape.time.domain.ReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/schema.sql"})
class ReservationDaoTest {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationTimeDao reservationTimeDao;
    @Autowired
    private ThemeDao themeDao;
    @Autowired
    private MemberDao memberDao;

    private final ReservationTime reservationTime = new ReservationTime(1L, "10:00");
    private final Member member = new Member(1L, "hotea", "hotea@hotea.com");
    private final Theme theme = new Theme(1L, "정글 모험", "열대 정글의 심연을 탐험하세요.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    private final Reservation reservation = new Reservation(1L, member, new ReservationDate(LocalDate.MAX.toString()), reservationTime, theme);

    @DisplayName("특정 예약을 생성할 수 있다.")
    @Test
    void save() {
        reservationTimeDao.save(reservationTime);
        themeDao.save(theme);

        assertThat(reservationDao.save(reservation)).isEqualTo(1L);
    }

    @DisplayName("예약을 모두 조회할 수 있다.")
    @Test
    void findAll() {
        Member memberWithPassword = new Member(1L, "hotea", "hotea@hotea.com", "password");
        reservationTimeDao.save(reservationTime);
        themeDao.save(theme);
        memberDao.save(memberWithPassword);
        reservationDao.save(reservation);

        List<Reservation> reservationList = reservationDao.findAll();

        assertAll(
                () -> assertThat(reservationList.get(0)).isEqualTo(reservation),
                () -> assertThat(reservationList.size()).isEqualTo(1)
        );
    }

    @DisplayName("특정 예약을 삭제 할 수 있다.")
    @Test
    void deleteById() {
        reservationTimeDao.save(reservationTime);
        themeDao.save(theme);
        reservationDao.save(reservation);

        assertThat(reservationDao.deleteById(1L)).isEqualTo(1);
    }

    @DisplayName("특정 날짜, 시간, 테마에 예약이 존재하는지 알 수 있다.")
    @Test
    void checkExistByReservation() {
        reservationTimeDao.save(reservationTime);
        themeDao.save(theme);
        reservationDao.save(reservation);

        assertThat(reservationDao.checkExistByReservation(
                reservation.getReservationDate().getDate(),
                reservationTime.getId(),
                theme.getId())
        ).isTrue();
    }

    @DisplayName("특정 테마 예약이 존재하는지 알 수 있다.")
    @Test
    void checkExistByReservationTheme() {
        reservationTimeDao.save(reservationTime);
        themeDao.save(theme);
        reservationDao.save(reservation);

        assertThat(reservationDao.checkExistReservationByTheme(
                theme.getId())
        ).isTrue();
    }

    @DisplayName("특정 시간 예약이 존재하는지 알 수 있다.")
    @Test
    void checkExistByReservationTime() {
        reservationTimeDao.save(reservationTime);
        themeDao.save(theme);
        reservationDao.save(reservation);

        assertThat(reservationDao.checkExistReservationByTime(
                reservationTime.getId())
        ).isTrue();
    }
}
