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

import roomescape.reservation.domain.Reservation;
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

    private final ReservationTime reservationTime = ReservationTime.createWithId(1L, "10:00");
    private final Theme theme = Theme.createWithId(1L, "정글 모험", "열대 정글의 심연을 탐험하세요.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    private final Reservation reservation = Reservation.createWithId(1L, "hotea", LocalDate.MAX.toString(), reservationTime, theme);

    @Test
    @DisplayName("특정 예약을 생성할 수 있다.")
    void save() {
        reservationTimeDao.save(reservationTime);
        themeDao.save(theme);
        assertThat(reservationDao.save(reservation)).isEqualTo(1L);
    }

    @Test
    @DisplayName("예약을 모두 조회할 수 있다.")
    void findAll() {
        save();
        List<Reservation> reservationList = reservationDao.findAll();
        assertAll(
                () -> assertThat(reservationList.get(0)).isEqualTo(reservation),
                () -> assertThat(reservationList.size()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("특정 예약을 삭제 할 수 있다.")
    void deleteById() {
        save();
        assertThat(reservationDao.deleteById(1L)).isEqualTo(1);
    }

    @Test
    @DisplayName("특정 날짜, 시간, 테마에 예약이 존재하는지 알 수 있다.")
    void checkExistByReservation() {
        save();
        assertThat(reservationDao.checkExistByReservation(
                reservation.getDate(),
                reservationTime.getId(),
                theme.getId())
        ).isTrue();
    }

    @Test
    @DisplayName("특정 테마 예약이 존재하는지 알 수 있다.")
    void checkExistByReservationTheme() {
        save();
        assertThat(reservationDao.checkExistReservationByTheme(
                theme.getId())
        ).isTrue();
    }

    @Test
    @DisplayName("특정 시간 예약이 존재하는지 알 수 있다.")
    void checkExistByReservationTime() {
        save();
        assertThat(reservationDao.checkExistReservationByTime(
                reservationTime.getId())
        ).isTrue();
    }
}
