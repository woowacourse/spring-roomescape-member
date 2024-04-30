package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import roomescape.exception.IllegalTimeException;

import java.sql.PreparedStatement;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("해당 ID를 가진 시간이 존재하지 않는다면 예외가 발생한다.")
    void findTimeById_AbsenceId_ExceptionThrown() {
        assertThatThrownBy(() -> reservationTimeService.findTimeById(0L))
                .isInstanceOf(IllegalTimeException.class);
    }

    @Test
    @DisplayName("예약이 존재하는 경우 예약시간을 삭제하면 예외가 발생한다.")
    void deleteTimeById_AbsenceId_ExceptionThrown() {
        long savedId = saveReservationTime();
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)", "테니", "13:00", savedId);

        assertThatThrownBy(() -> reservationTimeService.deleteTimeById(savedId))
                .isInstanceOf(IllegalTimeException.class);
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
