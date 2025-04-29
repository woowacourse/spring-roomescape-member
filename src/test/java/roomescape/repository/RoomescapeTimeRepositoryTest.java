package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.domain.ReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomescapeTimeRepositoryTest {

    @Autowired
    RoomescapeTimeRepository timeRepository;
    @Autowired
    JdbcTemplate template;

    @BeforeEach
    void setUp() {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "insert into reservation_time (start_at) values (?)";
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, "15:40");
            return ps;
        }, keyHolder);
    }

    @Test
    void findById() {
        //when
        ReservationTime time = timeRepository.findById(1L);

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
        ReservationTime reservationTime = ReservationTime.parse("16:30");

        //when
        ReservationTime saved = timeRepository.save(reservationTime);
        ReservationTime firstTime = timeRepository.findById(1L);
        ReservationTime secondTime = timeRepository.findById(2L);

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
        int deleteCounts = timeRepository.deleteById(1L);

        //then
        assertThat(deleteCounts).isEqualTo(1);
        assertThat(timeRepository.findAll()).isEmpty();
    }

}
