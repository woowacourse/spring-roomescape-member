package roomescape.dao.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;

public class JdbcReservationDaoTest {

    private static final LocalDate NOW_DATE = LocalDate.now().plusDays(1);

    private Member member;
    private ReservationTime reservationTime;
    private DataSource datasource;
    private JdbcTemplate jdbcTemplate;
    private JdbcReservationDaoImpl reservationDao;
    private Theme theme;

    @BeforeEach
    void init() {
        datasource = new EmbeddedDatabaseBuilder()
                .setName("testdb-" + UUID.randomUUID())
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(datasource);
        reservationDao = new JdbcReservationDaoImpl(jdbcTemplate);
    }

    @AfterEach
    void remove() {
        final String query = "delete from reservation";
        jdbcTemplate.update(query);
    }

    @DisplayName("reservation 객체가 주어졌을 때, db에 저장되며 id값이 부여되어야 한다.")
    @Test
    void given_reservation_then_save_db_and_set_id() {
        //given
        createFixture();

        final Reservation reservation = new Reservation(
                member,
                new ReservationDate(NOW_DATE),
                reservationTime,
                theme);

        //when
        reservationDao.saveReservation(reservation);

        //then
        assertThat(reservation.getId()).isEqualTo(1L);
        assertThat(reservationDao.findAllReservation().size()).isEqualTo(1);
    }

    @DisplayName("db에 존재하는 모든 reservation을 가져올 수 있어야 한다.")
    @Test
    void get_all_reservation() {
        //given
        createFixture();

        final Reservation reservation1 = new Reservation(
                member,
                new ReservationDate(NOW_DATE.plusDays(1)),
                reservationTime,
                theme);
        reservationDao.saveReservation(reservation1);

        final Reservation reservation2 = new Reservation(
                member,
                new ReservationDate(NOW_DATE.plusDays(2)),
                reservationTime,
                theme);
        reservationDao.saveReservation(reservation2);

        final Reservation reservation3 = new Reservation(
                member,
                new ReservationDate(NOW_DATE.plusDays(3)),
                reservationTime,
                theme);
        reservationDao.saveReservation(reservation3);

        //when, then
        assertThat(reservationDao.findAllReservation())
                .containsExactlyInAnyOrder(
                        reservation1,
                        reservation2,
                        reservation3);
        assertThat(reservationDao.findAllReservation().size()).isEqualTo(3);
    }

    @DisplayName("reservationId가 주어졌을 때, 해당하는 데이터를 삭제해야 한다.")
    @Test
    void given_reservation_id_then_delete_data() {
        //given
        createFixture();

        final Reservation reservation = new Reservation(
                member,
                new ReservationDate(NOW_DATE),
                reservationTime,
                theme);

        reservationDao.saveReservation(reservation);

        //when
        reservationDao.deleteReservation(1L);

        //then
        assertThat(reservationDao.findAllReservation().size()).isEqualTo(0);
    }

    @DisplayName("특정 테마, 기간 동안의 사용자 예약을 조회 할 수 있다.")
    @Test
    void findByCondition() {
        //given
        final Member member = Member.from(1L, "testName", "testEmail", "1234", MemberRole.ADMIN);
        final String memberInsertQuery = "insert into member (id, name, email, password, role) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(memberInsertQuery, 1L, "testName", "testEmail", "1234", "ADMIN");

        final ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        final String timeInsertQuery = "insert into reservation_time (id, start_at) values (?, ?)";
        jdbcTemplate.update(timeInsertQuery, 1L, "10:00");

        final Theme theme = new Theme(1L, "theme", "des", "thumb");
        final String themeInsertQuery = "insert into theme (id, name, description, thumbnail) values (?, ?, ?, ?)";
        jdbcTemplate.update(themeInsertQuery, 1L, "theme", "des", "thumb");

        reservationDao.saveReservation(
                new Reservation(
                        1L,
                        member,
                        new ReservationDate(LocalDate.of(2025, 5, 2)),
                        reservationTime,
                        theme
                )
        );

        reservationDao.saveReservation(
                new Reservation(
                        1L,
                        member,
                        new ReservationDate(LocalDate.of(2025, 5, 3)),
                        reservationTime,
                        theme
                )
        );

        reservationDao.saveReservation(
                new Reservation(
                        1L,
                        member,
                        new ReservationDate(LocalDate.of(2025, 5, 12)),
                        reservationTime,
                        theme
                )
        );

        final LocalDate dateFrom = LocalDate.of(2025, 5, 1);
        final LocalDate dateTo = LocalDate.of(2025, 5, 11);

        //when
        final List<Reservation> actual = reservationDao.findByThemeIdAndMemberIDAndDateFromAndDateTo(
                dateFrom,
                dateTo,
                theme.getId(),
                member.getId()
        );

        //then
        assertThat(actual).hasSize(2);
    }

    private void createFixture() {
        member = Member.from(1L, "testName", "testEmail", "1234", MemberRole.USER);
        reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        theme = new Theme(1L, "안녕 자두야", "hi", "https://aa");

        final String memberInsertQuery = "insert into member (id, name, email, password, role) values (?, ?, ?, ?, ?)";
        final String timeInsertQuery = "insert into reservation_time (id, start_at) values (?, ?)";
        final String themeInsertQuery = "insert into theme (id, name, description, thumbnail) values (?, ?, ?, ?)";

        jdbcTemplate.update(memberInsertQuery, 1L, "testName", "testEmail", "1234", "USER");
        jdbcTemplate.update(timeInsertQuery, 1L, "10:00");
        jdbcTemplate.update(themeInsertQuery, 1L, "안녕 자두야", "hi", "https://aa");
    }
}
