package roomescape.reservationtime.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @DisplayName("날짜/테마 선택 시 예약 가능한 시간 조회를 테스트 합니다.")
    @Test
    void find_available_times_by_date_and_theme() {
        Long themeId = 1L;
        LocalDate date = LocalDate.of(2026, 5, 4);

        Mockito.when(reservationTimeRepository.findByThemeAndDate(themeId, date)).thenReturn(List.of(
                ReservationTime.builder()
                        .id(1L)
                        .startAt(LocalTime.parse("09:00"))
                        .build(),
                ReservationTime.builder()
                        .id(2L)
                        .startAt(LocalTime.parse("10:00"))
                        .build()
        ));

        List<ReservationTimeResponse> reservationTimeResponses =  reservationTimeService.findAvailableTimes(themeId, date);

        Assertions.assertThat(reservationTimeResponses).containsExactly(
                new ReservationTimeResponse(1L, "09:00"),
                new ReservationTimeResponse(2L, "10:00")
        );
    }
}
