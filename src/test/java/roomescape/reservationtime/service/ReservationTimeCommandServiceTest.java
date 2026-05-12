package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.global.exception.NotFoundException;
import roomescape.reservationtime.application.dto.ReservationTimeCreateCommand;
import roomescape.reservationtime.application.dto.ReservationTimeResult;
import roomescape.global.exception.RoomEscapeException;
import roomescape.reservationtime.application.service.ReservationTimeCommandService;
import roomescape.reservationtime.infra.JdbcReservationTimeRepository;

@JdbcTest
@Import({ReservationTimeCommandService.class, JdbcReservationTimeRepository.class})
public class ReservationTimeCommandServiceTest {

    @Autowired
    private ReservationTimeCommandService timeCommandService;

    @DisplayName("예약 시간의 정상 추가를 테스트합니다.")
    @Test
    void save_time_successfully() {
        ReservationTimeResult result = timeCommandService.save(
                new ReservationTimeCreateCommand(LocalTime.of(9, 0))
        );

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result.id()).isPositive();
            softly.assertThat(result.startAt()).isEqualTo(LocalTime.of(9, 0));
        });
    }

    @DisplayName("중복된 예약 시간 추가 시 예외 발생을 테스트합니다.")
    @Test
    void save_duplicated_time_exception() {
        timeCommandService.save(new ReservationTimeCreateCommand(LocalTime.of(9, 0)));

        assertThatThrownBy(() -> timeCommandService.save(new ReservationTimeCreateCommand(LocalTime.of(9, 0))))
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("시간 09:00이(가) 이미 존재합니다.");
    }

    @DisplayName("예약 시간의 삭제를 테스트합니다.")
    @Test
    void delete_time() {
        ReservationTimeResult result = timeCommandService.save(
                new ReservationTimeCreateCommand(LocalTime.of(9, 0))
        );

        assertThatNoException().isThrownBy(() -> timeCommandService.delete(result.id()));
    }

    @DisplayName("삭제할 예약 시간이 없을 시 예외 발생을 테스트합니다.")
    @Test
    void fail_to_delete_theme() {
        assertThatThrownBy(() -> timeCommandService.delete(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 시간입니다.");
    }
}
