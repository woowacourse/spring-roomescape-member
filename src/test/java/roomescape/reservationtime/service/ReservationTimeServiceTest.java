package roomescape.reservationtime.service;

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
import roomescape.reservationtime.dto.ReservationTimeResponse;

class ReservationTimeServiceTest {

    private FakeReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
    private ReservationTimeService reservationTimeService = new ReservationTimeService(reservationTimeRepository);

    @BeforeEach
    void setUp() {
        reservationTimeRepository.save(ReservationTime.builder()
                .startAt(LocalTime.of(9, 0))
                .build()
        );
        reservationTimeRepository.save(ReservationTime.builder()
                .startAt(LocalTime.of(10, 0))
                .build()
        );
    }

    @DisplayName("날짜/테마 선택 시 예약 가능한 시간 조회를 테스트 합니다.")
    @Test
    void find_available_times_by_date_and_theme() {
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2026, 5, 6);

        List<ReservationTimeResponse> reservationTimeResponses =  reservationTimeService.findAvailableTimes(themeId, date);

        Assertions.assertThat(reservationTimeResponses).containsExactly(
                new ReservationTimeResponse(1L, "09:00"),
                new ReservationTimeResponse(2L, "10:00")
        );
    }

    @DisplayName("예약이 이미 되어있을 때 예약 가능한 시간 조회를 테스트 합니다.")
    @Test
    void find_available_times_if_one_reserved() {
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2026, 5, 6);

        reservationTimeRepository.saveReservation(Reservation.builder()
                .name("스타크")
                .date(date)
                .themeId(1L)
                .timeId(1L)
                .build()
        );

        List<ReservationTimeResponse> reservationTimeResponses =  reservationTimeService.findAvailableTimes(themeId, date);

        Assertions.assertThat(reservationTimeResponses).containsExactly(
                new ReservationTimeResponse(2L, "10:00")
        );
    }
}
