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
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;
import roomescape.util.TestDataSourceFactory;

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
        String dropSql = "DROP TABLE IF EXISTS reservation, reservation_time, theme";
        jdbcTemplate.execute(dropSql);
    }

    @DisplayName("Reservation 객체를 저장한다")
    @Test
    void save_and_return_id_test() {
        // given
        String name = "루키";
        LocalDate date = LocalDate.of(2024, 12, 31);
        ReservationTime time = new ReservationTime(6L, LocalTime.of(13, 15));
        Theme theme = new Theme(3L, "레벨1 탈출", "우테코 레벨1를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        Reservation reservation = new Reservation(null, name, date, time, theme);

        // when
        Long id = jdbcReservationDao.saveAndReturnId(reservation);

        // then
        String existsSql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE id = ?)";
        Boolean isExist = jdbcTemplate.queryForObject(existsSql, Boolean.class, id);
        assertAll(
                () -> assertThat(id).isEqualTo(17L),
                () -> assertThat(isExist).isTrue()
        );
    }

    @DisplayName("Reservation 데이터를 정상적으로 삭제한다")
    @Test
    void delete_by_id_test() {
        // given
        Long id = 3L;

        // when
        jdbcReservationDao.deleteById(id);

        // then
        String countSql = "SELECT COUNT(1) FROM reservation";
        Integer count = jdbcTemplate.queryForObject(countSql, Integer.class);

        String existsSql = "SELECT EXISTS(SELECT 1 FROM reservation WHERE id= ?)";
        Boolean isExist = jdbcTemplate.queryForObject(existsSql, Boolean.class, id);

        assertAll(
                () -> assertThat(count).isEqualTo(15),
                () -> assertThat(isExist).isFalse()
        );
    }

    @DisplayName("저장된 Reservation 목록들을 조회한다")
    @Test
    void find_all_test() {
        // when
        List<Reservation> reservations = jdbcReservationDao.findAll();

        // then
        Reservation reservation = reservations.get(0);
        assertAll(
                () -> assertThat(reservations).hasSize(16),
                () -> assertThat(reservation.getId()).isEqualTo(1L),
                () -> assertThat(reservation.getTheme().getId()).isEqualTo(1L)
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
                                LocalDate.of(2025, 4, 26),
                                LocalDate.of(2025, 4, 28),
                                LocalDate.of(2025, 5, 2),
                                LocalDate.of(2025, 5, 5)
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
                                LocalDate.of(2025, 5, 1),
                                LocalDate.of(2025, 5, 2)
                        )
        );
    }

}
