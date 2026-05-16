package roomescape.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.NotFoundException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ReservationCreatorTest {

    private final ReservationRepository reservationRepository = mock();
    private final ReservationTimeRepository reservationTimeRepository = mock();
    private final ThemeRepository themeRepository = mock();
    private final ReservationCreator creator = new ReservationCreator(
            reservationRepository,
            reservationTimeRepository,
            themeRepository);

    private final LocalDate date = LocalDate.now().plusDays(1);

    @Test
    void 예약을_생성한다() {
        // given
        Long id = 1L;
        Long timeId = 1L;
        Long themeId = 1L;
        String name = "브라운";
        ReservationTime time = new ReservationTime(timeId, LocalTime.parse("08:00"));
        Theme theme = new Theme(themeId, "테스트 테마", "테마 설명", "썸네일 주소");
        Reservation reservation = new Reservation(id, name, date, time, theme);

        when(reservationTimeRepository.findBy(timeId))
                .thenReturn(Optional.of(time));
        when(reservationRepository.existsWith(date, timeId, themeId))
                .thenReturn(false);
        when(themeRepository.findBy(themeId))
                .thenReturn(Optional.of(theme));
        when(reservationRepository.insert(any(Reservation.class)))
                .thenReturn(id);
        when(reservationRepository.findById(id))
                .thenReturn(Optional.of(reservation));

        // when
        Reservation result = creator.create(name, date, timeId, themeId);

        // then
        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(id),
                () -> assertThat(result.getName()).isEqualTo(name),
                () -> assertThat(result.getDate()).isEqualTo(date),
                () -> assertThat(result.getTime()).isEqualTo(time),
                () -> assertThat(result.getTheme()).isEqualTo(theme));

        verify(reservationTimeRepository).findBy(timeId);
        verify(reservationRepository).existsWith(date, timeId, themeId);
        verify(themeRepository).findBy(themeId);
        verify(reservationRepository).insert(captor.capture());
        Reservation captured = captor.getValue();

        assertAll(
                () -> assertThat(captured.getId()).isNull(),
                () -> assertThat(captured.getName()).isEqualTo(name),
                () -> assertThat(captured.getDate()).isEqualTo(date),
                () -> assertThat(captured.getTime()).isEqualTo(time),
                () -> assertThat(captured.getTheme()).isEqualTo(theme));

        verify(reservationRepository).findById(id);
    }

    @Test
    void 이미_예약된_시간이면_예약_생성시_예외_발생() {
        // given
        Long timeId = 1L;
        Long themeId = 1L;
        ReservationTime time = new ReservationTime(timeId, LocalTime.parse("08:00"));
        when(reservationTimeRepository.findBy(timeId))
                .thenReturn(Optional.of(time));
        when(reservationRepository.existsWith(date, timeId, themeId))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> creator.create("브라운", date, timeId, themeId))
                .isInstanceOf(DuplicateReservationException.class)
                .hasMessage("이미 예약된 시간입니다.");

        verify(reservationTimeRepository).findBy(timeId);
        verify(reservationRepository).existsWith(date, timeId, themeId);
        verifyNoInteractions(themeRepository);
        verify(reservationRepository, never()).insert(any(Reservation.class));
    }

    @Test
    void 존재하지_않는_timeId로_예약_생성시_예외_발생() {
        // given
        Long timeId = 999L;
        Long themeId = 1L;
        when(reservationTimeRepository.findBy(timeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> creator.create("홍길동", date, timeId, themeId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");

        verify(reservationTimeRepository).findBy(timeId);
        verify(reservationRepository, never()).existsWith(any(LocalDate.class), anyLong(), anyLong());
        verifyNoInteractions(themeRepository);
        verify(reservationRepository, never()).insert(any(Reservation.class));
    }

    @Test
    void 존재하지_않는_themeId로_예약_생성시_예외_발생() {
        // given
        Long timeId = 1L;
        Long themeId = 999L;
        ReservationTime time = new ReservationTime(timeId, LocalTime.parse("08:00"));
        when(reservationTimeRepository.findBy(timeId))
                .thenReturn(Optional.of(time));
        when(reservationRepository.existsWith(date, timeId, themeId))
                .thenReturn(false);
        when(themeRepository.findBy(themeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> creator.create("홍길동", date, timeId, themeId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 테마입니다.");

        verify(reservationTimeRepository).findBy(timeId);
        verify(reservationRepository).existsWith(date, timeId, themeId);
        verify(themeRepository).findBy(themeId);
        verify(reservationRepository, never()).insert(any(Reservation.class));
    }
}
