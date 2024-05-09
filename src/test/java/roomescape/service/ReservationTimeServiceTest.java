package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import roomescape.domain.reservation.service.ReservationTimeService;
import roomescape.global.exception.RoomEscapeException;

import java.sql.PreparedStatement;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("예약이 존재하는 경우 예약시간을 삭제하면 예외가 발생한다.")
    void deleteTimeById_AbsenceId_ExceptionThrown() {
        long savedId = saveReservationTime();
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id) VALUES (?, ?, ?)", 1L, "13:00", savedId);

        assertThatThrownBy(() -> reservationTimeService.deleteTimeById(savedId))
                .isInstanceOf(RoomEscapeException.class);
    }

    private long saveReservationTime() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO reservation_time (start_at) VALUES (?)",
                    new String[]{"id"});
            ps.setString(1, "15:40");
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
}
