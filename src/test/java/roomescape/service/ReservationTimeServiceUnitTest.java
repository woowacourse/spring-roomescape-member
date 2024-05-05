package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.ReservationTime;
import roomescape.dto.SaveReservationTimeRequest;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceUnitTest {

    @InjectMocks
    private ReservationTimeService reservationTimeService;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @DisplayName("전체 예약 시간 정보를 조회한다.")
    @Test
    void getReservationTimesTest() {
        // Given
        final List<ReservationTime> savedReservationTimes = List.of(
                new ReservationTime(1L, LocalTime.now().plusHours(3)),
                new ReservationTime(2L, LocalTime.now().plusHours(4))
        );

        given(reservationTimeRepository.findAll()).willReturn(savedReservationTimes);

        // When
        final List<ReservationTime> reservationTimes = reservationTimeService.getReservationTimes();

        // Then
        assertThat(reservationTimes).hasSize(savedReservationTimes.size());
    }

    @DisplayName("예약 시간 정보를 저장한다.")
    @Test
    void saveReservationTimeTest() {
        // Given
        final ReservationTime savedReservationTime = new ReservationTime(1L, LocalTime.now().plusHours(3));
        final SaveReservationTimeRequest saveReservationTimeRequest = new SaveReservationTimeRequest(LocalTime.now().plusHours(3));

        given(reservationTimeRepository.save(saveReservationTimeRequest.toReservationTime())).willReturn(savedReservationTime);

        // When
        final ReservationTime reservationTime = reservationTimeService.saveReservationTime(saveReservationTimeRequest);

        // Then
        assertThat(reservationTime).isEqualTo(savedReservationTime);
    }

    @DisplayName("예약 시간 정보를 삭제한다.")
    @Test
    void deleteReservationTimeTest() {
        // Given
        given(reservationTimeRepository.deleteById(1L)).willReturn(1);

        // When & Then
        assertThatCode(() -> reservationTimeService.deleteReservationTime(1L))
                .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 예약 시간 정보를 삭제하려고 하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenDeleteNotExistReservationTimeTest() {
        // Given
        given(reservationTimeRepository.deleteById(1L)).willReturn(0);

        // When & Then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 id의 예약 시간이 존재하지 않습니다.");
    }

    @DisplayName("이미 존재하는 예약시간이 입력되면 예외를 발생한다.")
    @Test
    void throwExceptionWhenExistReservationTimeTest() {
        // Given
        final LocalTime startAt = LocalTime.now().plusHours(3);
        final SaveReservationTimeRequest saveReservationTimeRequest = new SaveReservationTimeRequest(startAt);

        given(reservationTimeRepository.existByStartAt(startAt)).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> reservationTimeService.saveReservationTime(saveReservationTimeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 예약시간이 있습니다.");
    }

    @DisplayName("해당 시간을 참조하고 있는 예약이 하나라도 있으면 삭제시 예외가 발생한다.")
    @Test
    void throwExceptionWhenDeleteReservationTimeHasRelation() {
        // Given
        final Long reservationTimeId = 1L;
        given(reservationRepository.existByTimeId(reservationTimeId)).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(reservationTimeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약에 포함된 시간 정보는 삭제할 수 없습니다.");
    }
}
