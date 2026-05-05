package roomescape.reservation.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
public class JdbcReservationTimeRepositoryTest {
    @Autowired
    private JdbcReservationTimeRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 시간_저장_레포지토리_테스트() {
        ReservationTime savedReservationTime = repository.save(LocalTime.of(15, 40));
        Long id = jdbcTemplate.queryForObject("SELECT id FROM reservation_time LIMIT 1", Long.class);

        assertThat(savedReservationTime.getId()).isEqualTo(id);
        assertThat(savedReservationTime.getStartAt()).isEqualTo(LocalTime.of(15, 40));
    }

    @Test
    void 전체_시간_조회_레포지토리_테스트() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "15:00");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "16:00");

        List<ReservationTime> reservationTimes = repository.findAll();

        assertThat(reservationTimes).hasSize(2);
        assertThat(reservationTimes)
                .extracting(ReservationTime::getStartAt)
                .containsExactly(LocalTime.of(15, 00), LocalTime.of(16, 00));
    }

    @Test
    void 시간_삭제_레포지토리_테스트() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "15:00");
        Long id = jdbcTemplate.queryForObject("SELECT id FROM reservation_time LIMIT 1", Long.class);

        repository.deleteById(id);
        int rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation_time", Integer.class);

        assertThat(rowCount).isEqualTo(0);
    }

    @Test
    @DisplayName("일정표에 등록된 특정 날짜와 테마의 모든 시간을 가져올 수 있다.")
    void findTimesDateAndThemeId_테스트() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail_url) VALUES (?,?,?)", "꿀잼 방탈출",
                "재밌는 분위기의 방탈출", "https://example.com/theme_happy.jpg");
        Long themeId = jdbcTemplate.queryForObject("SELECT id FROM theme WHERE name = ?", Long.class, "꿀잼 방탈출");

        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "15:00");
        Long timeId = jdbcTemplate.queryForObject("SELECT id FROM reservation_time LIMIT 1", Long.class);

        jdbcTemplate.update("INSERT INTO schedule (date, time_id, theme_id) VALUES (?, ?, ?)", "2026-05-05", timeId, themeId);

        List<ReservationTime> result = repository.findTimesByDateAndThemeId(LocalDate.parse("2026-05-05"), themeId);

        assertThat(result)
                .extracting(ReservationTime::getStartAt)
                .containsExactly(LocalTime.of(15, 00));
    }
}
