package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.exception.ErrorMessage;
import roomescape.exception.custom.PastReservationTimeException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationCommandServiceTest {

    @Autowired
    private ReservationCommandService reservationCommandService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
    }

    @Test
    @DisplayName("지나간 날짜, 시간에 대한 예약 시 예외가 발생한다.")
    void throwExceptionWhenPastDateReservation() {
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at, status) VALUES (1, '10:00', 'AVAILABLE')");
        jdbcTemplate.update("INSERT INTO theme (id, name, thumbnail_url, description, status) VALUES (1, '공포의 저택', 'url', '설명', 'AVAILABLE')");

        LocalDate date = LocalDate.of(2026, 5, 1);
        long timeId = 1L;
        long themeId = 1L;
        LocalDateTime requestDateTime = LocalDateTime.of(date, LocalTime.MAX);

        assertThatThrownBy(() -> reservationCommandService.create("user_a", date, timeId, themeId, requestDateTime))
                .isExactlyInstanceOf(PastReservationTimeException.class)
                .hasMessage(ErrorMessage.CANNOT_SELECT_PAST_DATETIME.getMessage());
    }

    @Test
    @DisplayName("중복 예약 시 예외가 발생한다")
    void throwExceptionWhenDuplicateReservation() {
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at, status) VALUES (1, '10:00', 'AVAILABLE')");
        jdbcTemplate.update("INSERT INTO theme (id, name, thumbnail_url, description, status) VALUES (1, '공포의 저택', 'url', '설명', 'AVAILABLE')");

        LocalDate date = LocalDate.of(2026, 5, 1);
        long timeId = 1L;
        long themeId = 1L;
        LocalDateTime requestDateTime = LocalDateTime.of(date, LocalTime.MIN);

        reservationCommandService.create("user_a", date, timeId, themeId, requestDateTime);

        assertThatThrownBy(() -> reservationCommandService.create("user_b", date, timeId, themeId, requestDateTime))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 중복된 예약이 존재합니다.");
    }
}
