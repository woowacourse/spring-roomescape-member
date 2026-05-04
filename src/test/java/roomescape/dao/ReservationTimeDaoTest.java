package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import roomescape.domain.ReservationTime;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationTimeDaoTest {

    private ReservationTimeDao dao;

    @BeforeEach
    void setup() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:reservation_time_dao_test;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        populator.execute(dataSource);

        jdbcTemplate.update("DELETE FROM reservation_time;");
        this.dao = new ReservationTimeDao(jdbcTemplate);
    }

    @Test
    void 시간_추가_테스트() {
        // given
        ReservationTime time = new ReservationTime(null, "08:00");

        // when
        Long id = dao.insert(time);

        // then
        List<ReservationTime> times = dao.findAll();
        ReservationTime savedTime = dao.findBy(id);
        assertAll(
                () -> assertThat(id).isNotNull(),
                () -> assertThat(times).hasSize(1),
                () -> assertThat(savedTime.getStartAt()).isEqualTo(time.getStartAt()));
    }

    @Test
    void 예약_삭제_테스트() {
        // given
        ReservationTime time1 = new ReservationTime(null, "08:00");
        ReservationTime time2 = new ReservationTime(null, "21:00");
        Long id1 = dao.insert(time1);
        Long id2 = dao.insert(time2);

        // when
        int deletedCount = dao.delete(id1);

        // then
        List<ReservationTime> times = dao.findAll();
        assertAll(
                () -> assertThat(deletedCount).isEqualTo(1),
                () -> assertThat(times).hasSize(1),
                () -> assertThatThrownBy(() -> dao.findBy(id1))
                        .isInstanceOf(EmptyResultDataAccessException.class));
    }
}
