package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.request.ReservationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationDuplicateTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("이미 존재하는 예약 건과 중복으로 예약하면 예외가 발생한다")
    void throwsException_whenDuplicateReservationExists() {
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", 1, "10:00");
        jdbcTemplate.update("INSERT INTO theme (id, name, description, url) VALUES (?, ?, ?, ?)", 1, "테마1", "설명", "url");


        jdbcTemplate.update("""
                INSERT INTO reservation (name, date, time_id, theme_id)
                VALUES (?, ?, ?, ?)
            """, "브라운", "2026-05-04", 1, 1);

        ReservationRequest request = new ReservationRequest("아나키", LocalDate.of(2026, 5, 4), 1L, 1L);

        assertThatThrownBy(() -> reservationService.save(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 예약 건입니다.");
    }
}
