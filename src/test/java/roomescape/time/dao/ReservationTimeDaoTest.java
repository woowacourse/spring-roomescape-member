package roomescape.time.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationUserTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/schema.sql"})
class ReservationTimeDaoTest {

    @Autowired
    private ReservationTimeDao reservationTimeDao;
    @Autowired
    private ThemeDao themeDao;
    @Autowired
    private ReservationDao reservationDao;

    private final ReservationTime reservationTime = new ReservationTime("09:00");

    @DisplayName("예약 시간을 생성할 수 있다.")
    @Test
    void save() {
        assertThat(reservationTimeDao.save(reservationTime).getId()).isEqualTo(1L);
    }

    @DisplayName("특정 예약 시간을 조회할 수 있다.")
    @Test
    void getById() {
        reservationTimeDao.save(reservationTime);

        assertThat(reservationTimeDao.getById(1L)).isEqualTo(new ReservationTime(1L, "09:00"));
    }

    @DisplayName("모든 예약 시간을 조회할 수 있다.")
    @Test
    void findAll() {
        reservationTimeDao.save(reservationTime);

        assertAll(
                () -> assertThat(reservationTimeDao.findAll()
                        .size()).isEqualTo(1)
        );
    }

    @DisplayName("특정 예약 시간을 삭제할 수 있다.")
    @Test
    void deleteById() {
        reservationTimeDao.save(reservationTime);

        assertThat(reservationTimeDao.deleteById(1L)).isEqualTo(1);
    }

    @DisplayName("날짜와 테마를 통해 가능한 예약을 찾을 수 있다.")
    @Test
    void findAvailableTime() {
        Theme theme = new Theme(1L, "테마 이름", "테마 설명", "테마 썸네일");
        ReservationTime bookedReservationTime = new ReservationTime(2L, "11:00");
        Member member = new Member(1L, "hotea", "hotea@hotea.com", Role.USER);
        Reservation reservation = new Reservation(member, new ReservationDate("2024-04-01"), bookedReservationTime, theme);
        reservationTimeDao.save(reservationTime);
        reservationTimeDao.save(bookedReservationTime);
        themeDao.save(theme);
        reservationDao.save(reservation);


        List<ReservationUserTime> userTimes = List.of(
                new ReservationUserTime(1L, "09:00", false),
                new ReservationUserTime(2L, "11:00", true)
        );

        assertThat(reservationTimeDao.findAvailableTime("2024-04-01", 1)).isEqualTo(userTimes);
    }

    @DisplayName("특정 예약 시간이 이미 존재하는 지 알 수 있다.")
    @Test
    void checkExistTime() {
        reservationTimeDao.save(reservationTime);

        assertThat(reservationTimeDao.checkExistTime(new ReservationTime(1L, "09:00"))).isTrue();
    }
}
