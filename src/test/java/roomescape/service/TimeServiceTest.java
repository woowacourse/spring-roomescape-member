package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.time.TimeRequest;
import roomescape.controller.time.TimeResponse;
import roomescape.service.exception.TimeUsedException;
import roomescape.repository.H2ReservationRepository;
import roomescape.repository.H2ReservationTimeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql(scripts = {"/drop.sql", "/schema.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@JdbcTest
@Import({TimeService.class, H2ReservationRepository.class, H2ReservationTimeRepository.class})
class TimeServiceTest {

    @Autowired
    private TimeService timeService;

    @Test
    @DisplayName("예약 시간 목록을 조회한다.")
    void getTimes() {
        // given
        List<TimeResponse> expected = List.of(
                new TimeResponse(1L, "10:15", false),
                new TimeResponse(2L, "11:20", false),
                new TimeResponse(3L, "12:25", false)
        );

        // when
        List<TimeResponse> actual = timeService.getTimes();

        // then
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    @Test
    @DisplayName("예약 시간을 추가한다.")
    void addTIme() {
        // given
        TimeRequest request = new TimeRequest("13:30");
        TimeResponse expected = new TimeResponse(4L, "13:30", false);

        // when
        TimeResponse actual = timeService.addTime(request);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("존재하는 예약 시간을 삭제한다.")
    void deleteTimePresent() {
        // given
        Long id = 3L;

        // when & then
        assertThat(timeService.deleteTime(id)).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간을 삭제할 경우 반환 값이 0이다.")
    void deleteTImeNotPresent() {
        // given
        Long id = 4L;

        // when & then
        assertThat(timeService.deleteTime(id)).isEqualTo(0);
    }

    @Test
    @DisplayName("예약이 있는 시간을 삭제할 경우 예외가 발생한다.")
    void invalidDelete() {
        assertThatThrownBy(() -> timeService.deleteTime(2L))
                .isInstanceOf(TimeUsedException.class);
    }
}
