package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.reservationtime.domain.AvailableTime;
import roomescape.reservationtime.domain.ReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class UserReservationTimeServiceTest {

    @Autowired
    private UserReservationTimeService userReservationTimeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("DELETE FROM reservation");
        jdbcTemplate.execute("DELETE FROM reservation_time");
        jdbcTemplate.execute("DELETE FROM themes");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
        jdbcTemplate.execute("ALTER TABLE themes ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");

        // time(id=1~5), theme(id=1)
        jdbcTemplate.update("INSERT INTO themes (name, description, thumbnail) VALUES ('Theme A', 'Desc', 'https://a.png')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('11:00:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('12:00:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('13:00:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('14:00:00')");
        // 2099-12-31에 time(id=1)만 예약 → isAvailable=false, time(id=2)는 예약 없음 → isAvailable=true
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('User', '2099-12-31', 1, 1)");
    }

    @Test
    void 예약_시간_목록을_조회할_수_있다() {
        List<ReservationTime> times = userReservationTimeService.getReservationTimes();

        assertThat(times).hasSize(5);
    }

    @Test
    void 스케줄_목록을_조회할_수_있다() {
        List<AvailableTime> schedules = userReservationTimeService.getSchedules(LocalDate.of(2099, 12, 31), 1L);

        assertThat(schedules).hasSize(5);
        assertThat(schedules.stream().filter(t -> t.timeId() == 1).findFirst().get().isAvailable()).isFalse();
        assertThat(schedules.stream().filter(t -> t.timeId() == 2).findFirst().get().isAvailable()).isTrue();
    }

    @Test
    void 과거_날짜의_스케줄은_예약_여부와_관계없이_모두_불가능하다() {
        List<AvailableTime> schedules = userReservationTimeService.getSchedules(LocalDate.now().minusDays(1), 1L);

        assertThat(schedules).allMatch(t -> !t.isAvailable());
    }
}