package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.reservation.ReservationRepository;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.reservationtime.ReservationTimeRepository;
import roomescape.domain.reservationtime.AvailableReservationTimeDto;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.AvailableReservationTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("모든 예약 시간들을 조회한다.")
    void getAllReservationTimes() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        BDDMockito.given(reservationTimeRepository.findAll())
                .willReturn(List.of(reservationTime));

        List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.getAllReservationTimes();

        ReservationTimeResponse expected = ReservationTimeResponse.from(reservationTime);
        assertThat(reservationTimeResponses).containsExactly(expected);
    }

    @Test
    @DisplayName("예약 시간을 추가한다.")
    void addReservationTime() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.of(10, 0));
        BDDMockito.given(reservationTimeRepository.existsByStartAt(any()))
                .willReturn(false);
        BDDMockito.given(reservationTimeRepository.save(any()))
                .willReturn(reservationTime);

        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest("10:00");
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.addReservationTime(
                reservationTimeRequest);

        ReservationTimeResponse expected = new ReservationTimeResponse(1L, LocalTime.of(10, 0));
        assertThat(reservationTimeResponse).isEqualTo(expected);
    }

    @Test
    @DisplayName("이미 존재하는 예약 시간을 추가할 수 없다.")
    void addExistingReservationTime() {
        BDDMockito.given(reservationTimeRepository.existsByStartAt(any()))
                .willReturn(true);

        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest("10:00");

        assertThatThrownBy(() -> reservationTimeService.addReservationTime(reservationTimeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간은 이미 존재합니다.");
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    void deleteReservationTimeById() {
        BDDMockito.given(reservationTimeRepository.existsById(anyLong()))
                .willReturn(true);
        BDDMockito.given(reservationRepository.existsByTimeId(anyLong()))
                .willReturn(false);

        reservationTimeService.deleteReservationTimeById(1L);

        BDDMockito.verify(reservationTimeRepository).deleteById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간을 삭제할 수 없다.")
    void deleteNotExistedReservationTime() {
        BDDMockito.given(reservationTimeRepository.existsById(anyLong()))
                .willReturn(false);

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTimeById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 id의 시간이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("사용 중인 예약 시간을 삭제할 수 없다.")
    void deleteReservationTimeInUse() {
        BDDMockito.given(reservationTimeRepository.existsById(anyLong()))
                .willReturn(true);
        BDDMockito.given(reservationRepository.existsByTimeId(anyLong()))
                .willReturn(true);

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTimeById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간을 사용하는 예약이 존재합니다.");
    }

    @Test
    @DisplayName("특정 날짜에 사용 가능한 예약 시간을 조회한다.")
    void getAvailableReservationTimes() {
        AvailableReservationTimeDto availableReservationTimeDto1 = new AvailableReservationTimeDto(1L,
                LocalTime.of(10, 0), true);
        AvailableReservationTimeDto availableReservationTimeDto2 = new AvailableReservationTimeDto(2L,
                LocalTime.of(12, 0), false);
        BDDMockito.given(reservationTimeRepository.findAvailableReservationTimes(any(), anyLong()))
                .willReturn(List.of(availableReservationTimeDto1, availableReservationTimeDto2));

        List<AvailableReservationTimeResponse> availableTimes = reservationTimeService.getAvailableReservationTimes(
                LocalDate.of(2024, 5, 1), 1L);

        assertThat(availableTimes).containsExactly(
                new AvailableReservationTimeResponse(1L, LocalTime.of(10, 0), true),
                new AvailableReservationTimeResponse(2L, LocalTime.of(12, 0), false)
        );
    }
}
