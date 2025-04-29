package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.model.ReservationTime;

class JdbcReservationTimeDaoTest {

    private static JdbcReservationTimeDao dao;
    private static JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("schema.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new JdbcReservationTimeDao(jdbcTemplate);
    }

    @Test
    void 예약시간_저장() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(10,0));
        ReservationTime saved = dao.save(reservationTime);
        List<ReservationTime> all = dao.findAll();

        assertThat(all).hasSize(1);
        assertThat(all.getFirst().getId()).isEqualTo(saved.getId());
        assertThat(all.getFirst().getStartAt()).isEqualTo(saved.getStartAt());
    }

    @Test
    void 예약시간_삭제() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(10,0));
        ReservationTime saved = dao.save(reservationTime);
        boolean isDeleted = dao.deleteById(saved.getId());

        List<ReservationTime> all = dao.findAll();
        assertThat(isDeleted).isTrue();
        assertThat(all).isEmpty();
    }

    @Test
    void id로_예약시간_조회() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(10,0));
        ReservationTime saved = dao.save(reservationTime);

        ReservationTime foundReservationTime = dao.findById(saved.getId());
        assertThat(foundReservationTime.getId()).isEqualTo(saved.getId());
        assertThat(foundReservationTime.getStartAt()).isEqualTo(saved.getStartAt());
    }
}
