package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

class JdbcReservationDaoTest {
    private static JdbcReservationDao reservationDao;
    private static JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("schema.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        reservationDao = new JdbcReservationDao(jdbcTemplate);
        jdbcTemplate.execute("INSERT INTO reservation_time(start_at) VALUES ('10:00')");
        jdbcTemplate.execute("INSERT INTO theme(name, description, thumbnail) VALUES ('hello', 'description', 'thumbnail')");
    }

    @Test
    void 예약_저장() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "hello", "description", "thumbnail");
        Reservation reservation = new Reservation(null, "이름", LocalDate.of(2025,12,16), reservationTime, theme);

        Reservation saved = reservationDao.save(reservation);
        List<Reservation> all = reservationDao.findAll();

        assertThat(all).hasSize(1);
        assertThat(all.getFirst().getId()).isEqualTo(saved.getId());
        assertThat(all.getFirst().getName()).isEqualTo(saved.getName());
        assertThat(all.getFirst().getDate()).isEqualTo(saved.getDate());
        assertThat(all.getFirst().getReservationTime().getId()).isEqualTo(saved.getReservationTime().getId());
    }

    @Test
    void 예약_삭제() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        Theme theme = new Theme(1L, "hello", "description", "thumbnail");
        Reservation reservation = new Reservation(null, "이름", LocalDate.of(2025,12,16), reservationTime, theme);

        Reservation saved = reservationDao.save(reservation);
        boolean isDeleted = reservationDao.deleteById(saved.getId());
        List<Reservation> all = reservationDao.findAll();

        assertThat(isDeleted).isTrue();
        assertThat(all).hasSize(0);
    }
}
