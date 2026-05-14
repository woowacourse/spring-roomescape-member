package roomescape.reservation.scheduler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.ReservationStatus;
import roomescape.reservation.service.ReservationService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ReservationSchedulerTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long timeId;
    private Long themeId;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('09:00:00')");
        timeId = jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_at = '09:00:00'", Long.class);

        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail_url) VALUES (?,?,?)",
                "테스트 테마", "설명", "https://example.com/img.jpg");
        themeId = jdbcTemplate.queryForObject(
                "SELECT id FROM theme WHERE name = '테스트 테마'", Long.class);
    }

    @Test
    @DisplayName("과거 시각의 RESERVED 예약은 COMPLETED 로 변경된다")
    void pastReservedBecomesCompleted() {
        // given
        insertReservation("브라운", LocalDate.now().minusDays(1), "RESERVED");

        // when
        int updated = reservationService.completeExpiredReservations();

        // then
        assertThat(updated).isGreaterThanOrEqualTo(1);

        String status = jdbcTemplate.queryForObject(
                "SELECT status FROM reservation WHERE name = '브라운' AND theme_id = ?",
                String.class, themeId);
        assertThat(status).isEqualTo(ReservationStatus.COMPLETED.name());
    }

    @Test
    @DisplayName("미래 RESERVED 예약은 변경되지 않는다")
    void futureReservedIsUntouched() {
        // given
        insertReservation("브라운", LocalDate.now().plusDays(7), "RESERVED");

        // when
        reservationService.completeExpiredReservations();

        // then
        String status = jdbcTemplate.queryForObject(
                "SELECT status FROM reservation WHERE name = '브라운' AND theme_id = ?",
                String.class, themeId);
        assertThat(status).isEqualTo(ReservationStatus.RESERVED.name());
    }

    @Test
    @DisplayName("이미 CANCELED 된 과거 예약은 변경되지 않는다")
    void canceledIsUntouched() {
        // given
        insertReservation("브라운", LocalDate.now().minusDays(3), "CANCELED");

        // when
        reservationService.completeExpiredReservations();

        // then
        String status = jdbcTemplate.queryForObject(
                "SELECT status FROM reservation WHERE name = '브라운' AND theme_id = ?",
                String.class, themeId);
        assertThat(status).isEqualTo(ReservationStatus.CANCELED.name());
    }

    @Test
    @DisplayName("이미 COMPLETED 된 예약은 중복 업데이트되지 않는다")
    void alreadyCompletedIsUntouched() {
        // given
        insertReservation("브라운", LocalDate.now().minusDays(2), "COMPLETED");

        // when
        int updated = reservationService.completeExpiredReservations();

        // then
        assertThat(updated).isEqualTo(0);
    }

    @Test
    @DisplayName("과거 RESERVED 여러 건을 한 번에 일괄 처리한다")
    void bulkUpdateMultiplePastReservations() {
        // given
        insertReservation("검프", LocalDate.now().minusDays(1), "RESERVED");
        insertReservation("류시", LocalDate.now().minusDays(2), "RESERVED");
        insertReservation("포비", LocalDate.now().minusDays(3), "RESERVED");
        insertReservation("브라운",  LocalDate.now().plusDays(1),  "RESERVED");

        // when
        int updated = reservationService.completeExpiredReservations();

        // then
        assertThat(updated).isGreaterThanOrEqualTo(3);

        long completedCount = fetchAll().stream()
                .filter(r -> "COMPLETED".equals(r.get("STATUS")))
                .count();
        assertThat(completedCount).isGreaterThanOrEqualTo(3);

        String futureStatus = jdbcTemplate.queryForObject(
                "SELECT status FROM reservation WHERE name = '브라운' AND theme_id = ?",
                String.class, themeId);
        assertThat(futureStatus).isEqualTo("RESERVED");
    }

    private void insertReservation(String name, LocalDate date, String status) {
        jdbcTemplate.update(
                "INSERT INTO reservation (name, reservation_date, time_id, theme_id, status) VALUES (?,?,?,?,?)",
                name, date, timeId, themeId, status);
    }

    private List<Map<String, Object>> fetchAll() {
        return jdbcTemplate.queryForList(
                "SELECT name, status FROM reservation WHERE theme_id = ? ORDER BY reservation_date",
                themeId);
    }
}
