package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.member.dao.JdbcMemberDao;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.dao.JdbcReservationTimeDao;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.dao.JdbcThemeDao;
import roomescape.theme.domain.Theme;

@JdbcTest
@Import({JdbcReservationTimeDao.class, JdbcReservationDao.class, JdbcThemeDao.class, JdbcMemberDao.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Sql(scripts = "/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class JdbcReservationDaoTest {

    @Autowired
    private JdbcReservationDao jdbcReservationDao;

    @Autowired
    private JdbcReservationTimeDao jdbcReservationTimeDao;

    @Autowired
    private JdbcMemberDao jdbcMemberDao;

    @Autowired
    private JdbcThemeDao jdbcThemeDao;

    @DisplayName("에약을 데이터베이스에 추가한다.")
    @Test
    void addTest() {

        // given
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(
                ReservationTime.create(LocalTime.now().plusHours(1)));
        Theme savedTheme = jdbcThemeDao.create(Theme.create("test", "test", "test"));
        Member savedMember = jdbcMemberDao.create(
                Member.createWithoutId("test", "test@test.com", "qwer1234!", Role.USER));
        Reservation reservation = Reservation.create(LocalDate.now(), savedMember,
                savedReservationTime, savedTheme);

        // when
        Reservation savedReservation = jdbcReservationDao.create(reservation);

        // then
        assertThat(savedReservation.getMember().getName()).isEqualTo("test");
    }

    @DisplayName("에약을 데이터베이스에서 조회한다.")
    @Test
    void findAllTest() {

        // given
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(
                ReservationTime.create(LocalTime.now().plusHours(1)));
        Theme savedTheme = jdbcThemeDao.create(Theme.create("test", "test", "test"));
        Member savedMember = jdbcMemberDao.create(
                Member.createWithoutId("test", "test@test.com", "qwer1234!", Role.USER));
        Reservation reservation1 = Reservation.create(LocalDate.now(), savedMember,
                savedReservationTime, savedTheme);
        Reservation reservation2 = Reservation.create(LocalDate.now(), savedMember,
                savedReservationTime, savedTheme);
        jdbcReservationDao.create(reservation1);
        jdbcReservationDao.create(reservation2);

        // when
        List<Reservation> reservations = jdbcReservationDao.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(2);
    }

    @DisplayName("예약을 삭제한다.")
    @Test
    void deleteTest() {

        // given
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(
                ReservationTime.create(LocalTime.now().plusHours(1)));
        Theme savedTheme = jdbcThemeDao.create(Theme.create("test", "test", "test"));
        Member savedMember = jdbcMemberDao.create(
                Member.createWithoutId("test", "test@test.com", "qwer1234!", Role.USER));
        Reservation reservation = Reservation.create(LocalDate.now(), savedMember, savedReservationTime, savedTheme);
        Reservation savedReservation = jdbcReservationDao.create(reservation);

        // when
        jdbcReservationDao.delete(savedReservation.getId());
        List<Reservation> reservations = jdbcReservationDao.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(0);
    }

    @DisplayName("특정 테마, 시간, 날짜로 예약을 찾는다.")
    @Test
    void findByThemeAndDateAndTimeTest() {

        //given
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(
                ReservationTime.create(LocalTime.now().plusHours(1)));
        Theme savedTheme = jdbcThemeDao.create(Theme.create("test", "test", "test"));
        Member savedMember = jdbcMemberDao.create(
                Member.createWithoutId("test", "test@test.com", "qwer1234!", Role.USER));
        Reservation reservation = Reservation.create(LocalDate.now(), savedMember, savedReservationTime, savedTheme);
        Reservation savedReservation = jdbcReservationDao.create(reservation);

        // when
        Optional<Reservation> optionalReservation = jdbcReservationDao.findByThemeAndDateAndTime(savedReservation);

        // then
        assertAll(
                () -> assertThat(optionalReservation.isPresent()).isTrue(),
                () -> assertThat(optionalReservation.get()).isEqualTo(savedReservation)
        );
    }

    @DisplayName("타임 아이디에 해당하는 예약이 있으면 찾는다")
    @Test
    void findByTimeId_True() {

        // given
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(
                ReservationTime.create(LocalTime.now().plusHours(1)));
        Theme savedTheme = jdbcThemeDao.create(Theme.create("test", "test", "test"));
        Member savedMember = jdbcMemberDao.create(
                Member.createWithoutId("test", "test@test.com", "qwer1234!", Role.USER));
        Reservation reservation = Reservation.create(LocalDate.now(), savedMember, savedReservationTime, savedTheme);
        jdbcReservationDao.create(reservation);

        // when
        Optional<Reservation> reservationOptional = jdbcReservationDao.findByTimeId(savedReservationTime.getId());

        // then
        assertThat(reservationOptional.isPresent()).isTrue();
    }

    @DisplayName("타임 아이디에 해당하는 예약이 존재하지 않으면 빈 Optinal을 반환한다")
    @Test
    void findByTimeId_False() {

        // given
        ReservationTime reservationTime = ReservationTime.create(LocalTime.now().plusHours(1));
        jdbcReservationTimeDao.create(reservationTime);

        // when
        Optional<Reservation> reservationOptional = jdbcReservationDao.findByTimeId(100L);

        // then
        assertThat(reservationOptional.isPresent()).isFalse();
    }

    @DisplayName("테마 아이디에 해당하는 예약이 있으면 찾는다")
    @Test
    void findByThemeId_True() {

        // given
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(
                ReservationTime.create(LocalTime.now().plusHours(1)));
        Theme savedTheme = jdbcThemeDao.create(Theme.create("test", "test", "test"));
        Member savedMember = jdbcMemberDao.create(
                Member.createWithoutId("test", "test@test.com", "qwer1234!", Role.USER));
        Reservation reservation = Reservation.create(LocalDate.now(), savedMember, savedReservationTime, savedTheme);
        jdbcReservationDao.create(reservation);

        // when
        Optional<Reservation> reservationOptional = jdbcReservationDao.findByThemeId(savedTheme.getId());

        // then
        assertThat(reservationOptional.isPresent()).isTrue();
    }

    @DisplayName("테마 아이디에 해당하는 예약이 존재하지 않으면 빈 Optinal을 반환한다")
    @Test
    void findByThemeId_False() {

        // given
        Theme theme = Theme.create("test", "test", "test");
        jdbcThemeDao.create(theme);

        // when
        Optional<Reservation> reservationOptional = jdbcReservationDao.findByThemeId(100L);

        // then
        assertThat(reservationOptional.isPresent()).isFalse();
    }
}
