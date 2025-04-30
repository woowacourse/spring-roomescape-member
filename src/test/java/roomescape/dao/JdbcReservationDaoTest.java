package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.dao.jdbc.JdbcReservationDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
@Import(JdbcReservationDao.class)
public class JdbcReservationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcReservationDao jdbcReservationDao;

    @Test
    @DisplayName("DataSource 접근 테스트")
    void connectJdbc() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null)
                .next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("전체 예약 기록을 조회할 수 있다.")
    void findAllReservation() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('15:40')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('16:40')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('레벨1', '탈출하기', 'http/~')");
        jdbcTemplate.update(
            "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('사나', '2024-04-22', 1, 1)");
        jdbcTemplate.update(
            "INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('프리', '2024-04-23', 2, 1)");

        List<Reservation> reservations = jdbcReservationDao.findAllReservations();

        assertThat(reservations).hasSize(2);
    }

    @Test
    @DisplayName("예약을 추가할 수 있다.")
    void addReservation() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "레벨1", "탈출하기", "http/~");
        Reservation reservation = new Reservation(null, "사나", LocalDate.of(2024, 4, 22), time, theme);

        Reservation newReservation = jdbcReservationDao.addReservation(reservation);

        assertThat(newReservation).isNotNull();
    }

    @Test
    @DisplayName("ID로 예약을 삭제할 수 있다.")
    void removeReservation() {
        ReservationTime time = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "레벨1", "탈출하기", "http/~");
        Reservation reservation = new Reservation(null, "사나", LocalDate.of(2024, 4, 22), time, theme);

        Reservation newReservation = jdbcReservationDao.addReservation(reservation);
        jdbcReservationDao.removeReservationById(newReservation.getId());

        assertThat(jdbcReservationDao.findAllReservations()).isEmpty();
    }

    @Test
    @DisplayName("날짜, 시간, 테마에 해당되는 예약이 존재하는지 확인할 수 있다.")
    void existTimeByStartAt() {
        LocalDate date = LocalDate.of(2024, 12, 31);
        ReservationTime time = new ReservationTime(1L, LocalTime.of(12, 0));
        Theme theme = new Theme(1L, "레벨1", "탈출하기", "http/~");

        Reservation reservation = new Reservation(null, "사나", date, time, theme);
        jdbcReservationDao.addReservation(reservation);

        assertThat(jdbcReservationDao.existReservationByDateTimeAndTheme(date, 1L, 1L)).isTrue();
    }
}
