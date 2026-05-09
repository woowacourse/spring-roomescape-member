package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

@JdbcTest
class ReservationTimeServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time;");
        jdbcTemplate.update("DELETE FROM theme");
        ReservationTimeRepository reservationTimeRepository = new ReservationTimeRepository(jdbcTemplate);
        ReservationRepository reservationRepository = new ReservationRepository(jdbcTemplate);
        this.reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
    }

    @Test
    void 시간_생성_테스트() {
        // when
        ReservationTime result = reservationTimeService.create(LocalTime.of(8, 0));

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getStartAt()).isEqualTo(LocalTime.of(8, 0))
        );
    }

    @Test
    void 전체_시간_조회_테스트() {
        // given
        reservationTimeService.create(LocalTime.of(8, 0));
        reservationTimeService.create(LocalTime.of(21, 0));

        // when
        List<ReservationTime> result = reservationTimeService.findAll();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 시간_삭제_테스트() {
        // given
        ReservationTime created = reservationTimeService.create(LocalTime.of(8, 0));

        // when
        reservationTimeService.delete(created.getId());

        // then
        assertThat(reservationTimeService.findAll()).isEmpty();
    }

    @Test
    void 존재하지않는_time_id_삭제_시_예외_발생() {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.delete(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("[ERROR] 존재하지 않는 ID입니다.");
    }

    @Test
    void 예약된_시간대_삭제_시_예외_발생() {
        //given
        LocalDate date = LocalDate.parse("2026-05-05");
        Long timeId = createReservationTime();
        Long themeId = createTheme();
        createReservation(date, timeId, themeId);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.delete(timeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 해당 시간에 예약이 존재합니다.");
    }

    private Long createTheme() {
        jdbcTemplate.update(
                "INSERT INTO theme(name, description, thumbnail) VALUES (?, ?, ?)",
                "테스트 테마", "테스트 테마 설명", "테스트 테마 url"
        );
        return jdbcTemplate.queryForObject(
                "SELECT id FROM theme ORDER BY id DESC LIMIT 1",
                Long.class
        );
    }

    private Long createReservationTime() {
        jdbcTemplate.update(
                "INSERT INTO reservation_time(start_at) VALUES (?)",
                "10:00"
        );
        return jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time ORDER BY id DESC LIMIT 1",
                Long.class
        );
    }

    private void createReservation(LocalDate date, Long timeId, Long themeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation(name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "브라운", date, timeId, themeId
        );
    }
}
