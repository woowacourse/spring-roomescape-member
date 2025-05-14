package roomescape.reservationTime.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
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
import roomescape.reservation.dao.JdbcReservationDao;
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
class JdbcReservationTimeDaoTest {

    @Autowired
    JdbcMemberDao jdbcMemberDao;
    @Autowired
    private JdbcReservationTimeDao jdbcReservationTimeDao;
    @Autowired
    private JdbcReservationDao jdbcReservationDao;
    @Autowired
    private JdbcThemeDao jdbcThemeDao;

    @DisplayName("예약 시간을 데이터베이스에 추가한다.")
    @Test
    void addTest() {

        // given
        ReservationTime reservationTime = ReservationTime.create(LocalTime.of(10, 10));

        // when
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);

        // then
        assertThat(savedReservationTime.getStartAt()).isEqualTo(LocalTime.of(10, 10));
    }

    @DisplayName("데이터이스에 있는 예약 시간 정보들을 가져온다.")
    @Test
    void findAllTest() {

        // given
        ReservationTime reservationTime1 = ReservationTime.create(LocalTime.of(10, 10));
        jdbcReservationTimeDao.create(reservationTime1);
        ReservationTime reservationTime2 = ReservationTime.create(LocalTime.of(11, 10));
        jdbcReservationTimeDao.create(reservationTime2);

        // when
        List<ReservationTime> reservationTimes = jdbcReservationTimeDao.findAll();

        // then
        assertThat(reservationTimes.size()).isEqualTo(2);
    }

    @DisplayName("데이터베이스에서 예약 시간이 삭제될 경우 true를 반환한다.")
    @Test
    void deleteIfNoExistReservationReturnTrueTest() {

        // given
        ReservationTime reservationTime = ReservationTime.create(LocalTime.of(10, 10));
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);

        // when & then
        assertThatCode(() -> jdbcReservationTimeDao.delete(savedReservationTime))
                .doesNotThrowAnyException();
    }

    @DisplayName("데이터베이스에서 id, 테마, 날짜로 시간을 찾는다.")
    @Test
    void findByIdAndDateAndThemeTest() {

        // given
        LocalTime time = LocalTime.of(10, 10);
        LocalDate date = LocalDate.now().plusDays(1);
        Theme theme = Theme.create("test", "test", "test");
        ReservationTime savedReservationTime = jdbcReservationTimeDao.create(ReservationTime.create(time));
        Theme savedTheme = jdbcThemeDao.create(theme);
        Member savedMember = jdbcMemberDao.create(
                Member.createWithoutId("test", "test@test.com", "qwer1234!", Role.USER));
        Reservation reservation = Reservation.create(date, savedMember, savedReservationTime, savedTheme);
        jdbcReservationDao.create(reservation);

        // when
        Optional<ReservationTime> optionalReservationTime = jdbcReservationTimeDao.findByIdAndDateAndTheme(
                savedReservationTime.getId(), savedTheme.getId(), date);

        // then
        assertAll(
                () -> assertThat(optionalReservationTime.isPresent()).isTrue(),
                () -> assertThat(optionalReservationTime.get()).isEqualTo(savedReservationTime)
        );
    }
}
