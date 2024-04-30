package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ReservationTimeSaveRequest;
import roomescape.exception.ResourceNotFoundException;
import roomescape.model.ReservationTime;
import roomescape.repository.ReservationTimeRepository;
import roomescape.testutil.ReservationTimeMemoryRepository;

class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void init() {
        final ReservationTimeRepository reservationTimeRepository = new ReservationTimeMemoryRepository();
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("11:00")));
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("12:00")));
        reservationTimeService = new ReservationTimeService(reservationTimeRepository);
    }

    @DisplayName("예약 시간 목록 조회")
    @Test
    void getTimes() {
        final List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.getTimes();
        assertThat(reservationTimeResponses.size()).isEqualTo(2);
    }

    @DisplayName("예약 시간 추가")
    @Test
    void saveTime() {
        final ReservationTimeSaveRequest reservationTimeSaveRequest = new ReservationTimeSaveRequest(LocalTime.parse("01:00"));
        final ReservationTimeResponse reservationTimeResponse = reservationTimeService.saveTime(reservationTimeSaveRequest);
        assertThat(reservationTimeResponse).isEqualTo(new ReservationTimeResponse(3L, LocalTime.parse("01:00")));
    }

    @DisplayName("예약 시간 삭제")
    @Test
    void deleteTime() {
        reservationTimeService.deleteTime(1L);
        assertThat(reservationTimeService.getTimes().size()).isEqualTo(1);
    }

    @DisplayName("존재하지 않는 예약 시간 삭제")
    @Test
    void deleteTimeNotFound() {
        assertThatThrownBy(() -> reservationTimeService.deleteTime(3L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

