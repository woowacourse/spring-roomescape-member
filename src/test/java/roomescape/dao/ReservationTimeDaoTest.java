package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.model.ReservationTime;

class ReservationTimeDaoTest {

    private static ReservationTimeDao dao;
    private static JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(dataSource);
        dao = new ReservationTimeDao(jdbcTemplate);
    }

    @Test
    void 예약시간_저장() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(23, 59));
        ReservationTime saved = dao.save(reservationTime);
        List<ReservationTime> all = dao.findAll();

        assertThat(all).hasSize(5);
        assertThat(all.getLast().getId()).isEqualTo(saved.getId());
        assertThat(all.getLast().getStartAt()).isEqualTo(saved.getStartAt());
    }

    @Test
    void 예약시간_삭제() {
        boolean isDeleted = dao.deleteById(4L);

        List<ReservationTime> all = dao.findAll();
        assertThat(isDeleted).isTrue();
        assertThat(all).hasSize(3);
    }

    @Test
    void 삭제할_id가_없는_경우() {
        // given
        Long id = 999L;

        // when
        boolean isDeleted = dao.deleteById(id);

        // then
        List<ReservationTime> all = dao.findAll();
        assertThat(isDeleted).isFalse();
        assertThat(all).hasSize(4);
    }

    @Test
    void id로_예약시간_조회() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.of(10, 0));
        ReservationTime saved = dao.save(reservationTime);

        Optional<ReservationTime> foundReservationTime = dao.findById(saved.getId());
        assertThat(foundReservationTime).isPresent();
        assertThat(foundReservationTime.get().getId()).isEqualTo(saved.getId());
        assertThat(foundReservationTime.get().getStartAt()).isEqualTo(saved.getStartAt());
    }

    @Test
    void id_로_조회_시_예약_시간이_없는_경우() {
        // given
        Long id = 99L;

        // when
        Optional<ReservationTime> reservationTime = dao.findById(id);

        // then
        assertThat(reservationTime).isEmpty();
    }

    @Test
    void 모든_예약_시간_반환() {
        // when
        List<ReservationTime> all = dao.findAll();

        // then
        assertThat(all).hasSize(4);
    }

    @Test
    void 예약시간_중복_여부_확인() {
        // given
        LocalTime existTime = LocalTime.of(13, 40);
        LocalTime nonExistTime = LocalTime.of(0, 1);

        // when
        boolean exist = dao.isExistTime(existTime);
        boolean nonExist = dao.isExistTime(nonExistTime);

        // then
        assertThat(exist).isTrue();
        assertThat(nonExist).isFalse();
    }
}
