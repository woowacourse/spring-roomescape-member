package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.AvailableReservationTimeResponse;
import roomescape.reservationtime.dto.ReservationTimeCreateRequest;
import roomescape.reservationtime.exception.ReservationTimeException;

class ReservationTimeServiceTest {

    private FakeReservationTimeRepository timeRepository = new FakeReservationTimeRepository();
    private ReservationTimeService timeService = new ReservationTimeService(timeRepository);

    @BeforeEach
    void setUp() {
        timeRepository.save(ReservationTime.builder()
                .startAt(LocalTime.of(9, 0))
                .build()
        );
        timeRepository.save(ReservationTime.builder()
                .startAt(LocalTime.of(10, 0))
                .build()
        );
    }

    @DisplayName("날짜/테마 선택 시 예약 가능한 시간 조회를 테스트 합니다.")
    @Test
    void find_available_times_by_date_and_theme() {
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2026, 5, 6);

        List<AvailableReservationTimeResponse> timeResponses = timeService.findAvailableTimes(themeId, date);

        Assertions.assertThat(timeResponses).containsExactly(
                new AvailableReservationTimeResponse(1L, LocalTime.of(9,0), true),
                new AvailableReservationTimeResponse(2L, LocalTime.of(10,0), true)
        );
    }

    @DisplayName("예약이 이미 되어있을 때 예약 가능한 시간 조회를 테스트 합니다.")
    @Test
    void find_available_times_if_one_reserved() {
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2026, 5, 6);

        timeRepository.saveReservation(Reservation.builder()
                .name("스타크")
                .date(date)
                .themeId(1L)
                .timeId(1L)
                .build()
        );

        List<AvailableReservationTimeResponse> timeResponses = timeService.findAvailableTimes(themeId, date);

        Assertions.assertThat(timeResponses).containsExactly(
                new AvailableReservationTimeResponse(1L, LocalTime.of(9,0), false),
                new AvailableReservationTimeResponse(2L, LocalTime.of(10,0), true)
        );
    }

    @DisplayName("중복된 타임 추가 시 예외 발생을 테스트합니다.")
    @Test
    void save_duplicated_time_exception() {
        ReservationTimeCreateRequest createRequestDto = new ReservationTimeCreateRequest(LocalTime.of(9, 0));

        assertThatThrownBy(() -> timeService.save(createRequestDto))
                .isInstanceOf(ReservationTimeException.class)
                .hasMessage("[ERROR] 시간 09:00이(가) 이미 존재합니다.");
    }
}
