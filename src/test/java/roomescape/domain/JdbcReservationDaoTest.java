package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.JdbcReservationDao;
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
    void dropTable(){
        String dropSql = "DROP TABLE IF EXISTS reservation, reservation_time, theme";
        jdbcTemplate.execute(dropSql);
    }

    @DisplayName("Reservation 객체를 저장한다")
    @Test
    void create_reservation_test() {
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
        assertThat(id).isEqualTo(6L);
    }

    @DisplayName("Reservation 데이터를 정상적으로 삭제한다")
    @Test
    void delete_reservation_test() {
        // given
        Long id = 3L;

        // when
        jdbcReservationDao.deleteById(id);

        String sql = "SELECT COUNT(*) FROM reservation";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);

        // then
        assertThat(count).isEqualTo(4);
    }

    @DisplayName("저장된 Reservation 목록들을 조회한다")
    @Test
    void get_reservations_test() {
        // when
        List<Reservation> reservations = jdbcReservationDao.findAll();

        // then
        assertThat(reservations).hasSize(5);
    }

    @DisplayName("시간 id 일치 여부를 반환한다")
    @Test
    void same_time_id_test() {
        // when
        Boolean actual = jdbcReservationDao.existReservationByTimeId(1L);
        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("시간과 날짜 일치 여부를 반환한다")
    @Test
    void same_time_id_and_date_test(){
        // when
        Boolean actual = jdbcReservationDao.existReservationByDateAndTimeIdAndThemeId(LocalDate.of(2025, 5, 5), 1L, 1L);
        // then
        assertThat(actual).isTrue();
    }
}
