package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.domain.ReservationTime;

@JdbcTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class RoomescapeTimeRepositoryTest {

    @Autowired
    DataSource dataSource;
    RoomescapeTimeRepository timeRepository;
    JdbcTemplate template;

    @BeforeEach
    void setUp() {
        template = new JdbcTemplate(dataSource);
        timeRepository = new RoomescapeTimeRepositoryImpl(dataSource);
        template.execute("DELETE FROM reservation");
        template.execute("DELETE FROM reservation_time");
        template.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        template.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        template.execute("insert into reservation_time (start_at) values ('15:40')");

    }

    @Test
    void findById() {
        //when
        ReservationTime time = timeRepository.findById(1L).get();

        //then
        assertThat(time.getStartAt()).isEqualTo(LocalTime.parse("15:40"));
    }

    @Test
    void findAll() {
        //when
        List<ReservationTime> times = timeRepository.findAll();

        //then
        ReservationTime time = times.getFirst();
        assertThat(times).hasSize(1);
        assertThat(time.getId()).isEqualTo(1L);
        assertThat(time.getStartAt()).isEqualTo(LocalTime.parse("15:40"));
    }

    @Test
    void save() {
        //given
        ReservationTime reservationTime = new ReservationTime("16:30");

        //when
        ReservationTime saved = timeRepository.save(reservationTime);
        ReservationTime firstTime = timeRepository.findById(1L).get();
        ReservationTime secondTime = timeRepository.findById(2L).get();

        //then
        assertThat(saved.getId()).isEqualTo(2L);
        assertThat(saved.getStartAt()).isEqualTo(LocalTime.parse("16:30"));
        assertThat(firstTime.getId()).isEqualTo(1L);
        assertThat(firstTime.getStartAt()).isEqualTo(LocalTime.parse("15:40"));
        assertThat(secondTime.getId()).isEqualTo(2L);
        assertThat(secondTime.getStartAt()).isEqualTo(LocalTime.parse("16:30"));
    }

    @Test
    void deleteById() {
        //when
        boolean result = timeRepository.deleteById(1L);

        //then
        assertThat(result).isTrue();
        assertThat(timeRepository.findAll()).isEmpty();
    }

}
