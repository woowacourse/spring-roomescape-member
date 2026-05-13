package roomescape.reservation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.dto.request.ReservationUpdateRequest;
import roomescape.reservation.dto.response.ReservationSaveResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.repository.projection.ReservationDetailProjection;
import roomescape.reservationtime.dto.response.TimeInformation;
import roomescape.schedule.ScheduleService;
import roomescape.theme.dto.response.ThemeFindResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("예약 삭제 시 삭제 건수가 0이면 예외가 발생하지 않는다.")
    void deleteById_삭제건수0_성공() {
        when(reservationRepository.deleteById(1L)).thenReturn(0);

        assertThatCode(() -> reservationService.deleteById(1L))
                .doesNotThrowAnyException();
        verify(reservationRepository).deleteById(1L);
    }

    @Test
    @DisplayName("예약 삭제 시 삭제 건수가 1이면 예외가 발생하지 않는다.")
    void deleteById_삭제건수1_성공() {
        when(reservationRepository.deleteById(1L)).thenReturn(1);

        assertThatCode(() -> reservationService.deleteById(1L))
                .doesNotThrowAnyException();
        verify(reservationRepository).deleteById(1L);
    }

    @Test
    @DisplayName("예약 삭제 시 삭제 건수가 2 이상이면 예외가 발생한다.")
    void deleteById_삭제건수2이상_실패() {
        when(reservationRepository.deleteById(1L)).thenReturn(2);

        assertThatThrownBy(() -> reservationService.deleteById(1L))
                .isInstanceOf(IllegalStateException.class);

        verify(reservationRepository).deleteById(1L);
    }

    @Test
    @DisplayName("이름 기준 예약 삭제 시 삭제 건수가 0이면 예외가 발생하지 않는다.")
    void deleteByIdAndName_삭제건수0_성공() {
        when(reservationRepository.deleteByIdAndName(1L, "a")).thenReturn(0);

        assertThatCode(() -> reservationService.deleteByIdAndName(1L, "a"))
                .doesNotThrowAnyException();
        verify(reservationRepository).deleteByIdAndName(1L, "a");
    }

    @Test
    @DisplayName("이름 기준 예약 삭제 시 삭제 건수가 1이면 예외가 발생하지 않는다.")
    void deleteByIdAndName_삭제건수1_성공() {
        when(reservationRepository.deleteByIdAndName(1L, "a")).thenReturn(1);

        assertThatCode(() -> reservationService.deleteByIdAndName(1L, "a"))
                .doesNotThrowAnyException();
        verify(reservationRepository).deleteByIdAndName(1L, "a");
    }

    @Test
    @DisplayName("이름 기준 예약 삭제 시 삭제 건수가 2 이상이면 예외가 발생한다.")
    void deleteByIdAndName_삭제건수2이상_실패() {
        when(reservationRepository.deleteByIdAndName(1L, "a")).thenReturn(2);

        assertThatThrownBy(() -> reservationService.deleteByIdAndName(1L, "a"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("예약 삭제에 실패했습니다. reservationId=1, name=a");
        verify(reservationRepository).deleteByIdAndName(1L, "a");
    }

    @Test
    @DisplayName("스케줄이 존재하고 해당 스케줄에 예약이 존재하지 않는다면 해당 스케줄로 예약을 변경할 수 있다.")
    void update_성공_레포지토리_테스트() {
        // given
        ReservationUpdateRequest request = new ReservationUpdateRequest(
                LocalDate.of(2026, 5, 5),
                4L
        );

        ReservationDetailProjection oldReservation = new ReservationDetailProjection(
                4L,
                "d",
                LocalDate.of(2026, 5, 5),
                new ThemeFindResponse(4L, "theme", "desc", "url"),
                new TimeInformation(3L, LocalTime.of(12, 0))
        );
        Reservation updatedReservation = new Reservation(4L, "d", 4L);

        when(reservationRepository.findDetailByIdAndName(4L, "d")).thenReturn(Optional.of(oldReservation));
        when(scheduleService.findScheduleIdByDateAndTimeIdAndThemeId(LocalDate.of(2026, 5, 5), 4L, 4L)).thenReturn(4L);
        when(reservationRepository.isDuplicateReservation(4L, 4L)).thenReturn(false);
        when(reservationRepository.updateScheduleByIdAndName(4L, "d", 4L)).thenReturn(1);
        when(reservationRepository.findByIdAndName(4L, "d")).thenReturn(Optional.of(updatedReservation));

        // when
        ReservationSaveResponse response = reservationService.update(request, 4L, "d");

        // then
        assertThat(response.id()).isEqualTo(4L);
        assertThat(response.name()).isEqualTo("d");
        assertThat(response.scheduleId()).isEqualTo(4L);
        verify(reservationRepository).updateScheduleByIdAndName(4L, "d", 4L);
    }

    @Test
    @DisplayName("스케줄이 존재하지만 해당 스케줄을 가진 다른 예약이 있을 경우 예외가 발생한다.")
    void update_실패_레포지토리_테스트1() {
        // given
        ReservationUpdateRequest request = new ReservationUpdateRequest(
                LocalDate.of(2026, 5, 5),
                2L
        );

        ReservationDetailProjection oldReservation = new ReservationDetailProjection(
                4L,
                "d",
                LocalDate.of(2026, 5, 6),
                new ThemeFindResponse(2L, "theme", "desc", "url"),
                new TimeInformation(2L, LocalTime.of(11, 0))
        );

        when(reservationRepository.findDetailByIdAndName(4L, "d")).thenReturn(Optional.of(oldReservation));
        when(scheduleService.findScheduleIdByDateAndTimeIdAndThemeId(LocalDate.of(2026, 5, 5), 2L, 2L)).thenReturn(2L);
        when(reservationRepository.isDuplicateReservation(4L, 2L)).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> reservationService.update(request, 4L, "d"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("중복된 예약이 있어 예약을 수정할 수 없습니다.");
        verify(reservationRepository, never()).updateScheduleByIdAndName(anyLong(), anyString(), anyLong());
    }

    @Test
    @DisplayName("내 이름으로 된 예약이 아닌데 변경을 시도할 시 예외가 발생한다.")
    void update_실패_레포지토리_테스트2() {
        // given
        ReservationUpdateRequest request = new ReservationUpdateRequest(
                LocalDate.of(2026, 5, 5),
                4L
        );
        when(reservationRepository.findDetailByIdAndName(4L, "x")).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> reservationService.update(request, 4L, "x"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 id와 name을 가진 예약이 존재하지 않습니다. id=4name=x");
        verify(reservationRepository, never()).updateScheduleByIdAndName(anyLong(), anyString(), anyLong());
    }

    @Test
    @DisplayName("없는 스케줄로 예약을 변경하려고 시도할 경우 예외가 발생한다.")
    void update_실패_레포지토리_테스트3() {
        // given
        ReservationUpdateRequest request = new ReservationUpdateRequest(
                LocalDate.of(2026, 5, 7),
                5L
        );
        ReservationDetailProjection oldReservation = new ReservationDetailProjection(
                4L,
                "d",
                LocalDate.of(2026, 5, 6),
                new ThemeFindResponse(2L, "theme", "desc", "url"),
                new TimeInformation(2L, LocalTime.of(11, 0))
        );

        when(reservationRepository.findDetailByIdAndName(4L, "d")).thenReturn(Optional.of(oldReservation));
        when(scheduleService.findScheduleIdByDateAndTimeIdAndThemeId(LocalDate.of(2026, 5, 7), 5L, 2L))
                .thenThrow(new IllegalArgumentException("해당 조건을 가진 일정이 없습니다. date: 2026-05-07timeid: 5themeId: 2"));

        // when, then
        assertThatThrownBy(() -> reservationService.update(request, 4L, "d"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 조건을 가진 일정이 없습니다. date: 2026-05-07timeid: 5themeId: 2");
        verify(reservationRepository, never()).updateScheduleByIdAndName(anyLong(), anyString(), anyLong());
    }
}
