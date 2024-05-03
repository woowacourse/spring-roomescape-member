package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.time.TimeRequest;
import roomescape.controller.time.TimeResponse;
import roomescape.repository.H2ReservationRepository;
import roomescape.repository.H2ReservationTimeRepository;
import roomescape.service.exception.TimeUsedException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql(scripts = {"/drop.sql", "/schema.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@JdbcTest
@Import({TimeService.class, H2ReservationTimeRepository.class, H2ReservationRepository.class})
class TimeServiceTest {

    final long LAST_ID = 4;
    final TimeResponse exampleFirstTime = new TimeResponse(1L, "10:15", false);

    @Autowired
    private TimeService timeService;

    @Test
    @DisplayName("예약 시간 목록을 조회한다.")
    void getTimes() {
        // given & when
        List<TimeResponse> actual = timeService.getTimes();

        // then
        assertThat(actual).hasSize((int) LAST_ID);
        assertThat(actual.get(0)).isEqualTo(exampleFirstTime);
    }

    @Test
    @DisplayName("예약 시간을 추가한다.")
    void addTIme() {
        // given
        TimeRequest request = new TimeRequest("14:35");

        // when
        TimeResponse actual = timeService.addTime(request);
        TimeResponse expected = new TimeResponse(actual.id(), "14:35", false);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void deleteTime() {
        // given & when & then
        assertThat(timeService.deleteTime(LAST_ID)).isOne();
        assertThat(timeService.deleteTime(LAST_ID)).isZero();
    }

    @Test
    @DisplayName("예약이 된 시간을 삭제할 경우 예외가 발생한다.")
    void invalidDelete() {
        assertThatThrownBy(() -> timeService.deleteTime(2L))
                .isInstanceOf(TimeUsedException.class);
    }
}
