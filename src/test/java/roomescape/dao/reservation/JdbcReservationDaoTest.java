package roomescape.dao.reservation;

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
import org.springframework.test.context.jdbc.Sql;
import roomescape.dao.meber.JdbcMemberDao;
import roomescape.dao.reservationTime.JdbcReservationTimeDao;
import roomescape.dao.theme.JdbcThemeDao;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import({JdbcReservationTimeDao.class, JdbcReservationDao.class, JdbcThemeDao.class, JdbcMemberDao.class})
@Sql({"/schema.sql", "/test-data.sql"})
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
        final ReservationTime reservationTime = new ReservationTime(LocalTime.now().plusHours(1));
        final ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);
        final Theme theme = new Theme("test", "test", "test");
        final Theme savedTheme = jdbcThemeDao.create(theme);
        final Member member = new Member(1L, "test", "test", "test", MemberRole.USER);
        final Reservation reservation = Reservation.create(LocalDate.now(),
                savedReservationTime, savedTheme, member);

        // when
        final Reservation savedReservation = jdbcReservationDao.create(reservation);

        // then
        assertThat(savedReservation.getMember().getId()).isEqualTo(1L);
    }

    @DisplayName("에약을 데이터베이스에서 조회한다.")
    @Test
    void findAllTest() {

        // when
        final List<Reservation> reservations = jdbcReservationDao.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(2);
    }

    @DisplayName("예약을 삭제한다.")
    @Test
    void deleteTest() {

        // when
        jdbcReservationDao.delete(1);
        final List<Reservation> reservations = jdbcReservationDao.findAll();

        // then
        assertThat(reservations.size()).isEqualTo(1);
    }

    @DisplayName("특정 테마, 시간, 날짜로 예약을 찾는다.")
    @Test
    void findByThemeAndDateAndTimeTest() {

        // given
        final Reservation reservation = jdbcReservationDao.findAll().getFirst();

        // when
        final Optional<Reservation> optionalReservation = jdbcReservationDao.findByThemeAndDateAndTime(reservation);

        // then
        assertAll(
                () -> assertThat(optionalReservation.isPresent()).isTrue(),
                () -> assertThat(optionalReservation.get()).isEqualTo(reservation)
        );
    }

    @DisplayName("데이터베이스에 존재할 경우 true를 반환한다.")
    @Test
    void existsByIdReturnTrueTest() {

        // when & then
        assertThat(jdbcReservationDao.existsById(1L)).isTrue();
    }

    @DisplayName("데이터베이스에 존재하지 않을 경우 false를 반환한다.")
    @Test
    void nonExistsByIdReturnFalseTest() {

        // when & then
        assertThat(jdbcReservationDao.existsById(5L)).isFalse();
    }
}
