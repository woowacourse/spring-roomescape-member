package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.domain.Member;
import roomescape.member.domain.enums.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.util.repository.TestDataSourceFactory;

class JdbcReservationDaoTest {

    private JdbcReservationDao jdbcReservationDao;
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        DataSource dataSource = TestDataSourceFactory.getEmbeddedDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcReservationDao = new JdbcReservationDao(jdbcTemplate);
    }

    @AfterEach
    void dropTable() {
        String dropSql = "DROP TABLE IF EXISTS reservation, reservation_time, theme, member";
        jdbcTemplate.execute(dropSql);
    }

    @DisplayName("Reservation 객체를 저장한다")
    @Test
    void save_and_return_id_test() {
        // given
        LocalDate date = LocalDate.of(2000, 1, 1);
        ReservationTime time = new ReservationTime(6L, LocalTime.of(13, 15));
        Theme theme = new Theme(3L, "레벨1 탈출", "우테코 레벨1를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        Member member = new Member(3L, "행성이", "woowa@woowa.com", "woowa123", Role.USER);
        Reservation reservation = new Reservation(null, date, time, theme, member);

        // when
        Long savedId = jdbcReservationDao.saveAndReturnId(reservation);

        // then
        Reservation savedReservation = jdbcReservationDao.findById(savedId).get();

        assertAll(
                () -> assertThat(savedReservation.getDate()).isEqualTo(LocalDate.of(2000, 1, 1)),
                () -> assertThat(savedReservation.getThemeId()).isEqualTo(3L),
                () -> assertThat(savedReservation.getTimeId()).isEqualTo(6L),
                () -> assertThat(savedReservation.getMemberId()).isEqualTo(3L)
        );
    }

    @DisplayName("Reservation 데이터를 삭제한다")
    @Test
    void delete_by_id_test() {
        // given
        Long id = 3L;

        // when
        jdbcReservationDao.deleteById(id);

        // then
        assertAll(
                () -> assertThat(jdbcReservationDao.findAll()).hasSize(15),
                () -> assertThat(jdbcReservationDao.findById(id).isEmpty()).isTrue()
        );

    }

    @DisplayName("해당하는 ID Reservation 정보를 조회한다")
    @Test
    void find_by_id_test() {
        // given
        Long findId = 10L;

        // when
        Reservation findReservation = jdbcReservationDao.findById(findId).get();

        // then
        assertAll(
                () -> assertThat(findReservation.getId()).isEqualTo(10L),
                () -> assertThat(findReservation.getTimeId()).isEqualTo(2L),
                () -> assertThat(findReservation.getThemeId()).isEqualTo(3L),
                () -> assertThat(findReservation.getDate()).isEqualTo(LocalDate.now())
        );
    }

    @DisplayName("저장된 Reservation 목록들을 조회한다")
    @Test
    void find_all_test() {
        // when
        List<Reservation> reservations = jdbcReservationDao.findAll();

        // then
        assertAll(
                () -> assertThat(reservations).hasSize(16),
                () -> assertThat(reservations).extracting(Reservation::getThemeId)
                        .contains(1L, 2L, 3L, 4L, 5L),
                () -> assertThat(reservations).extracting(Reservation::getTimeId)
                        .contains(1L, 2L, 3L, 4L)
        );

    }

    @DisplayName("예약 시간 ID에 해당하는 예약 목록을 반환한다")
    @Test
    void find_all_by_time_id_test() {
        // given
        Long timeId = 4L;

        // when
        List<Reservation> reservations = jdbcReservationDao.findAllByTimeId(timeId);

        // then
        assertAll(
                () -> assertThat(reservations).hasSize(4),
                () -> assertThat(reservations).extracting(Reservation::getThemeId)
                        .containsExactlyInAnyOrder(2L, 3L, 4L, 5L),
                () -> assertThat(reservations).extracting(Reservation::getDate)
                        .containsExactlyInAnyOrder(
                                LocalDate.now().minusDays(4),
                                LocalDate.now().minusDays(2),
                                LocalDate.now().plusDays(2),
                                LocalDate.now().plusDays(5)
                        )
        );

    }

    @DisplayName("테마 ID에 해당하는 예약 목록을 반환한다")
    @Test
    void find_all_by_theme_id_test() {
        // given
        Long themeId = 4L;

        // when
        List<Reservation> reservations = jdbcReservationDao.findAllByThemeId(themeId);

        // then
        assertAll(
                () -> assertThat(reservations).hasSize(2),
                () -> assertThat(reservations).extracting(Reservation::getTimeId)
                        .containsExactlyInAnyOrder(3L, 4L),
                () -> assertThat(reservations).extracting(Reservation::getDate)
                        .containsExactlyInAnyOrder(
                                LocalDate.now().plusDays(1),
                                LocalDate.now().plusDays(2)
                        )
        );
    }

}
