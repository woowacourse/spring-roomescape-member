package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.repository.JdbcReservationDao;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
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
        Reservation reservation = new Reservation(null, name, date, time);

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
        Boolean actual = jdbcReservationDao.existReservationByDateAndTimeId(LocalDate.of(2025,3,24),1L);
        // then
        assertThat(actual).isTrue();
    }
}
