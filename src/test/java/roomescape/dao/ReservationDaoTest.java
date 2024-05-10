package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.TestFixture.DATE_FIXTURE;
import static roomescape.TestFixture.EMAIL_FIXTURE;
import static roomescape.TestFixture.MEMBER_NAME_FIXTURE;
import static roomescape.TestFixture.MEMBER_PARAMETER_SOURCE;
import static roomescape.TestFixture.PASSWORD_FIXTURE;
import static roomescape.TestFixture.RESERVATION_TIME_PARAMETER_SOURCE;
import static roomescape.TestFixture.ROOM_THEME_PARAMETER_SOURCE;
import static roomescape.TestFixture.THEME_DESCRIPTION_FIXTURE;
import static roomescape.TestFixture.THEME_NAME_FIXTURE;
import static roomescape.TestFixture.THEME_THUMBNAIL_FIXTURE;
import static roomescape.TestFixture.TIME_FIXTURE;

import io.restassured.RestAssured;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.RoomTheme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationDaoTest {
    @LocalServerPort
    private int port;

    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM member");
    }

    @DisplayName("존재하는 모든 예약을 보여준다.")
    @Test
    void findAll() {
        assertThat(reservationDao.findAll()).isEmpty();
    }

    @DisplayName("날짜와 시간이 같은 예약이 존재하는지 여부를 반환한다.")
    @Test
    void duplicatedReservationTest() {
        // given
        Reservation reservation = createReservation(DATE_FIXTURE);
        boolean existsFalse = reservationDao.exists(
                DATE_FIXTURE, reservation.getTime().getId(), reservation.getTheme().getId());
        reservationDao.save(reservation);
        // when
        boolean existsTrue = reservationDao.exists(
                DATE_FIXTURE, reservation.getTime().getId(), reservation.getTheme().getId());
        // then
        assertAll(
                () -> assertThat(existsFalse).isFalse(),
                () -> assertThat(existsTrue).isTrue()
        );
    }

    @DisplayName("예약을 저장한다.")
    @Test
    void save() {
        // given
        Reservation reservation = createReservation(DATE_FIXTURE);
        // when
        Reservation savedReservation = reservationDao.save(reservation);
        // then
        assertAll(
                () -> assertThat(reservationDao.findAll()).hasSize(1),
                () -> assertThat(savedReservation.getMember().getId()).isEqualTo(
                        reservation.getMember().getId()),
                () -> assertThat(savedReservation.getDate()).isEqualTo(reservation.getDate()),
                () -> assertThat(savedReservation.getTime().getId())
                        .isEqualTo(reservation.getTime().getId()),
                () -> assertThat(savedReservation.getTheme().getId())
                        .isEqualTo(reservation.getTheme().getId())
        );
    }

    @DisplayName("해당 id의 예약을 삭제한다.")
    @Test
    void deleteById() {
        // given
        Reservation reservation = createReservation(DATE_FIXTURE);
        Reservation savedReservation = reservationDao.save(reservation);
        // when
        reservationDao.deleteById(savedReservation.getId());
        // then
        assertThat(reservationDao.findAll()).isEmpty();
    }

    @DisplayName("삭제 대상이 존재하면 true를 반환한다.")
    @Test
    void returnTrueWhenDeleted() {
        // given
        Reservation reservation = createReservation(DATE_FIXTURE);
        Reservation savedReservation = reservationDao.save(reservation);
        // when
        boolean deleted = reservationDao.deleteById(savedReservation.getId());
        // then
        assertThat(deleted).isTrue();
    }

    @DisplayName("삭제 대상이 존재하지 않으면 false를 반환한다.")
    @Test
    void returnFalseWhenNotDeleted() {
        // given & when
        boolean deleted = reservationDao.deleteById(1L);
        // then
        assertThat(deleted).isFalse();
    }

    private Reservation createReservation(LocalDate date) {
        Long memberId = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id")
                .withTableName("member")
                .executeAndReturnKey(MEMBER_PARAMETER_SOURCE)
                .longValue();
        Long timeId = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id")
                .withTableName("reservation_time")
                .executeAndReturnKey(RESERVATION_TIME_PARAMETER_SOURCE)
                .longValue();
        Long themeId = new SimpleJdbcInsert(jdbcTemplate)
                .usingGeneratedKeyColumns("id")
                .withTableName("theme")
                .executeAndReturnKey(ROOM_THEME_PARAMETER_SOURCE)
                .longValue();
        Member member = new Member(memberId, new Name(MEMBER_NAME_FIXTURE), Role.NORMAL,
                EMAIL_FIXTURE, PASSWORD_FIXTURE);
        ReservationTime reservationTime = new ReservationTime(timeId, TIME_FIXTURE);
        RoomTheme roomTheme = new RoomTheme(themeId, THEME_NAME_FIXTURE,
                THEME_DESCRIPTION_FIXTURE, THEME_THUMBNAIL_FIXTURE);
        return new Reservation(date, member, reservationTime, roomTheme);
    }
}
