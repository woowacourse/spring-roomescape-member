package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.exception.ErrorMessage;
import roomescape.exception.custom.ConflictException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationTimeCommandServiceTest {

    @Autowired
    private ReservationTimeCommandService reservationTimeCommandService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation_history");
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
    }

    @Test
    @DisplayName("이미 존재하는 시작 시간으로 등록 시 예외가 발생한다.")
    void throwExceptionWhenDuplicateStartAt() {
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at, status) VALUES (1, '10:00', 'AVAILABLE')");

        assertThatThrownBy(() -> reservationTimeCommandService.create(LocalTime.of(10, 0)))
                .isExactlyInstanceOf(ConflictException.class)
                .hasMessage(ErrorMessage.DUPLICATE_TIME.getMessage());
    }

    @Test
    @DisplayName("예약이 존재하는 시간 삭제 시 예외가 발생한다.")
    void throwExceptionWhenDeleteTimeInUse() {
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at, status) VALUES (1, '10:00', 'AVAILABLE')");
        jdbcTemplate.update("INSERT INTO theme (id, name, thumbnail_url, description, status) VALUES (1, '공포의 저택', 'url', '설명', 'AVAILABLE')");
        jdbcTemplate.update("INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (1, 'user_a', '2026-06-01', 1, 1)");

        assertThatThrownBy(() -> reservationTimeCommandService.delete(1L))
                .isExactlyInstanceOf(ConflictException.class)
                .hasMessage(ErrorMessage.TIME_IN_USE.getMessage());
    }
}
