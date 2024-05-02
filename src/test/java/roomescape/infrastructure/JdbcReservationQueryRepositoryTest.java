package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.dto.AvailableTimeDto;

@JdbcTest
@Import(JdbcReservationQueryRepository.class)
class JdbcReservationQueryRepositoryTest {

    @Autowired
    private JdbcReservationQueryRepository reservationQueryRepository;

    @DisplayName("날짜와 테마 id가 주어지면, 예약 가능한 시간을 반환한다.")
    @Test
    @Sql("/insert-reservations.sql")
    void shouldReturnAvailableTimes() {
        LocalDate date = LocalDate.of(2024, 12, 25);
        List<AvailableTimeDto> times = reservationQueryRepository.findAvailableReservationTimes(date, 1L)
                .stream()
                .filter(time -> !time.isBooked())
                .toList();
        assertThat(times).hasSize(3);
    }
}
