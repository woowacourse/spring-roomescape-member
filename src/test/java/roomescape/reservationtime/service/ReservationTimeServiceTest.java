package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.fixture.ReservationFixture;
import roomescape.fixture.fake.FakeReservationTimeRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.application.dto.AvailableReservationTimeQueryResult;
import roomescape.reservationtime.application.dto.ReservationTimeCreateCommand;
import roomescape.reservationtime.application.exception.ReservationTimeException;
import roomescape.reservationtime.application.service.ReservationTimeService;

class ReservationTimeServiceTest {

    private FakeReservationTimeRepository timeRepository;
    private ReservationTimeService timeService;

    @BeforeEach
    void setUp() {
        timeRepository = new FakeReservationTimeRepository();
        timeService = new ReservationTimeService(timeRepository);
        timeRepository.save(ReservationTime.builder().startAt(LocalTime.of(9, 0)).build());
        timeRepository.save(ReservationTime.builder().startAt(LocalTime.of(10, 0)).build());
    }

    @DisplayName("날짜/테마 선택 시 예약 가능한 시간 조회를 테스트 합니다.")
    @Test
    void find_available_times_by_date_and_theme() {
        List<AvailableReservationTimeQueryResult> timeResponses = timeService.findAvailableTimes(
                1L,
                LocalDate.of(2026, 5, 6)
        );

        Assertions.assertThat(timeResponses).containsExactly(
                new AvailableReservationTimeQueryResult(1L, LocalTime.of(9, 0), true),
                new AvailableReservationTimeQueryResult(2L, LocalTime.of(10, 0), true)
        );
    }

    @DisplayName("예약이 이미 되어있을 때 예약 가능한 시간 조회를 테스트 합니다.")
    @Test
    void find_available_times_if_one_reserved() {
        timeRepository.saveReservation(ReservationFixture.starkReservation(1L, 1L));

        List<AvailableReservationTimeQueryResult> timeResponses = timeService.findAvailableTimes(
                1L, LocalDate.of(2026, 5, 6)
        );

        Assertions.assertThat(timeResponses).containsExactly(
                new AvailableReservationTimeQueryResult(1L, LocalTime.of(9, 0), false),
                new AvailableReservationTimeQueryResult(2L, LocalTime.of(10, 0), true)
        );
    }

    @DisplayName("중복된 타임 추가 시 예외 발생을 테스트합니다.")
    @Test
    void save_duplicated_time_exception() {
        assertThatThrownBy(() -> timeService.save(new ReservationTimeCreateCommand(LocalTime.of(9, 0))))
                .isInstanceOf(ReservationTimeException.class)
                .hasMessage("시간 09:00이(가) 이미 존재합니다.");
    }
}