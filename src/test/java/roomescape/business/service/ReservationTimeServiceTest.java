package roomescape.business.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.business.model.entity.ReservationTime;
import roomescape.business.model.repository.ReservationRepository;
import roomescape.business.model.repository.ReservationTimeRepository;
import roomescape.exception.business.InvalidCreateArgumentException;
import roomescape.exception.business.NotFoundException;
import roomescape.exception.business.RelatedEntityExistException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationTimeServiceTest {

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationTimeService sut;

    @Test
    void 예약_시간을_추가하고_반환한다() {
        // given
        LocalTime time = LocalTime.of(10, 0);

        when(reservationTimeRepository.existByTime(time)).thenReturn(false);
        when(reservationTimeRepository.existBetween(any(LocalTime.class), any(LocalTime.class))).thenReturn(false);

        // when
        ReservationTime result = sut.addAndGet(time);

        // then
        assertThat(result).isNotNull();
        assertThat(result.startAt()).isEqualTo(time);
        verify(reservationTimeRepository).existByTime(time);
        verify(reservationTimeRepository).existBetween(any(LocalTime.class), any(LocalTime.class));
        verify(reservationTimeRepository).save(any(ReservationTime.class));
    }

    @Test
    void 중복된_시간으로_예약_시간_추가_시_예외가_발생한다() {
        // given
        LocalTime time = LocalTime.of(10, 0);

        when(reservationTimeRepository.existByTime(time)).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> sut.addAndGet(time))
                .isInstanceOf(InvalidCreateArgumentException.class);

        verify(reservationTimeRepository).existByTime(time);
        verify(reservationTimeRepository, never()).existBetween(any(LocalTime.class), any(LocalTime.class));
        verify(reservationTimeRepository, never()).save(any(ReservationTime.class));
    }

    @Test
    @DisplayName("시간 간격이 겹치는 예약 시간 추가 시 예외가 발생한다")
    void addAndGet_OverlappingInterval_ThrowsException() {
        // given
        LocalTime time = LocalTime.of(10, 0);

        when(reservationTimeRepository.existByTime(time)).thenReturn(false);
        when(reservationTimeRepository.existBetween(any(LocalTime.class), any(LocalTime.class))).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> sut.addAndGet(time))
                .isInstanceOf(InvalidCreateArgumentException.class);

        verify(reservationTimeRepository).existByTime(time);
        verify(reservationTimeRepository).existBetween(any(LocalTime.class), any(LocalTime.class));
        verify(reservationTimeRepository, never()).save(any(ReservationTime.class));
    }

    @Test
    @DisplayName("모든 예약 시간을 조회할 수 있다")
    void getAll_ReturnsAllReservationTimes() {
        // given
        List<ReservationTime> expectedTimes = Arrays.asList(
                ReservationTime.restore("time-id-1", LocalTime.of(10, 0)),
                ReservationTime.restore("time-id-2", LocalTime.of(14, 0))
        );

        when(reservationTimeRepository.findAll()).thenReturn(expectedTimes);

        // when
        List<ReservationTime> result = sut.getAll();

        // then
        assertThat(result).isEqualTo(expectedTimes);
        verify(reservationTimeRepository).findAll();
    }

    @Test
    @DisplayName("날짜와 테마 ID로 이용 가능한 예약 시간을 조회할 수 있다")
    void getAvailableReservationTimesByDateAndThemeId_ReturnsAvailableTimes() {
        // given
        LocalDate date = LocalDate.now();
        String themeId = "theme-id";
        List<ReservationTime> expectedTimes = Arrays.asList(
                ReservationTime.restore("time-id-3", LocalTime.of(11, 0)),
                ReservationTime.restore("time-id-4", LocalTime.of(15, 0))
        );

        when(reservationTimeRepository.findAvailableReservationTimesByDateAndThemeId(date, themeId))
                .thenReturn(expectedTimes);

        // when
        List<ReservationTime> result = sut.getAvailableReservationTimesByDateAndThemeId(date, themeId);

        // then
        assertThat(result).isEqualTo(expectedTimes);
        verify(reservationTimeRepository).findAvailableReservationTimesByDateAndThemeId(date, themeId);
    }

    @Test
    @DisplayName("예약 시간을 삭제할 수 있다")
    void delete_ExistingReservationTime_DeletesReservationTime() {
        // given
        String timeId = "time-id";

        when(reservationRepository.existByTimeId(timeId)).thenReturn(false);
        when(reservationTimeRepository.existById(timeId)).thenReturn(true);

        // when
        sut.delete(timeId);

        // then
        verify(reservationRepository).existByTimeId(timeId);
        verify(reservationTimeRepository).existById(timeId);
        verify(reservationTimeRepository).deleteById(timeId);
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간 삭제 시 예외가 발생한다")
    void delete_NonExistingReservationTime_ThrowsException() {
        // given
        String timeId = "non-existing-id";

        when(reservationRepository.existByTimeId(timeId)).thenReturn(false);
        when(reservationTimeRepository.existById(timeId)).thenReturn(false);

        // when, then
        assertThatThrownBy(() -> sut.delete(timeId))
                .isInstanceOf(NotFoundException.class);

        verify(reservationRepository).existByTimeId(timeId);
        verify(reservationTimeRepository).existById(timeId);
        verify(reservationTimeRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("예약이 연결된 예약 시간 삭제 시 예외가 발생한다")
    void delete_ReservationTimeWithReservations_ThrowsException() {
        // given
        String timeId = "time-with-reservations";

        when(reservationRepository.existByTimeId(timeId)).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> sut.delete(timeId))
                .isInstanceOf(RelatedEntityExistException.class);

        verify(reservationRepository).existByTimeId(timeId);
        verify(reservationTimeRepository, never()).existById(anyString());
        verify(reservationTimeRepository, never()).deleteById(anyString());
    }
}
