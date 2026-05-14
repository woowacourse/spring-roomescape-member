package roomescape.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.ForbiddenReservationException;
import roomescape.exception.InvalidInputException;
import roomescape.exception.NotFoundException;
import roomescape.exception.PastReservationException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.result.TimeAvailabilityResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ReservationServiceTest {

    private final ReservationRepository reservationRepository = mock();
    private final ReservationTimeRepository reservationTimeRepository = mock();
    private final ThemeRepository themeRepository = mock();
    private final ReservationService service = new ReservationService(
            reservationRepository,
            reservationTimeRepository,
            themeRepository);

    private final LocalDate date = LocalDate.now().plusDays(1);

    @Test
    void 전체_예약_조회_테스트() {
        // given
        ReservationTime time = new ReservationTime(1L, LocalTime.parse("08:00"));
        Theme theme = new Theme(1L, "테스트 테마", "테마 설명", "썸네일 주소");
        List<Reservation> reservations = List.of(
                new Reservation(1L, "브라운", date, time, theme),
                new Reservation(2L, "구구", date, time, theme));
        when(reservationRepository.findAll())
                .thenReturn(reservations);

        // when
        List<Reservation> result = service.findAll();

        // then
        assertThat(result).isEqualTo(reservations);
        verify(reservationRepository).findAll();
    }

    @Test
    void 이름으로_예약_목록을_조회한다() {
        // given
        String name = "브라운";
        ReservationTime time = new ReservationTime(1L, LocalTime.parse("08:00"));
        Theme theme = new Theme(1L, "테스트 테마", "테마 설명", "썸네일 주소");
        List<Reservation> reservations = List.of(
                new Reservation(1L, name, date, time, theme),
                new Reservation(2L, name, date.plusDays(1), time, theme));
        when(reservationRepository.findByName(name))
                .thenReturn(reservations);

        // when
        List<Reservation> result = service.findByName(name);

        // then
        assertThat(result).isEqualTo(reservations);
        verify(reservationRepository).findByName(name);
    }

    @Test
    void 사용자_예약_생성_테스트() {
        // given
        Long id = 1L;
        Long timeId = 1L;
        Long themeId = 1L;
        String name = "브라운";
        ReservationTime time = new ReservationTime(timeId, LocalTime.parse("08:00"));
        Theme theme = new Theme(themeId, "테스트 테마", "테마 설명", "썸네일 주소");
        Reservation reservation = new Reservation(id, name, date, time, theme);

        when(reservationRepository.existWith(date, timeId, themeId))
                .thenReturn(false);
        when(reservationTimeRepository.findBy(timeId))
                .thenReturn(Optional.of(time));
        when(themeRepository.findBy(themeId))
                .thenReturn(Optional.of(theme));
        when(reservationRepository.insert(any(Reservation.class)))
                .thenReturn(id);
        when(reservationRepository.findById(id))
                .thenReturn(Optional.of(reservation));

        // when
        Reservation result = service.createUserReservation(name, date, timeId, themeId);

        // then
        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);

        assertAll(
                () -> assertThat(result.getId()).isEqualTo(id),
                () -> assertThat(result.getName()).isEqualTo(name),
                () -> assertThat(result.getDate()).isEqualTo(date),
                () -> assertThat(result.getTime()).isEqualTo(time),
                () -> assertThat(result.getTheme()).isEqualTo(theme));

        verify(reservationRepository).existWith(date, timeId, themeId);
        verify(reservationTimeRepository).findBy(timeId);
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

    @ParameterizedTest
    @MethodSource("adminReservationDateTimes")
    void 관리자_예약_생성_테스트(LocalDate date, LocalTime startAt) {
        // given
        Long id = 1L;
        Long timeId = 1L;
        Long themeId = 1L;
        String name = "브라운";
        ReservationTime time = new ReservationTime(timeId, startAt);
        Theme theme = new Theme(themeId, "테스트 테마", "테마 설명", "썸네일 주소");
        Reservation reservation = new Reservation(id, name, date, time, theme);

        when(reservationTimeRepository.findBy(timeId))
                .thenReturn(Optional.of(time));
        when(reservationRepository.existWith(date, timeId, themeId))
                .thenReturn(false);
        when(themeRepository.findBy(themeId))
                .thenReturn(Optional.of(theme));
        when(reservationRepository.insert(any(Reservation.class)))
                .thenReturn(id);
        when(reservationRepository.findById(id))
                .thenReturn(Optional.of(reservation));

        // when
        Reservation result = service.createAdminReservation(name, date, timeId, themeId);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(id),
                () -> assertThat(result.getName()).isEqualTo(name),
                () -> assertThat(result.getDate()).isEqualTo(date),
                () -> assertThat(result.getTime()).isEqualTo(time),
                () -> assertThat(result.getTheme()).isEqualTo(theme));

        verify(reservationTimeRepository).findBy(timeId);
        verify(reservationRepository).existWith(date, timeId, themeId);
        verify(themeRepository).findBy(themeId);
        verify(reservationRepository).insert(any(Reservation.class));
        verify(reservationRepository).findById(id);
    }

    private static Stream<Arguments> adminReservationDateTimes() {
        return Stream.of(
                Arguments.of(LocalDate.now().minusDays(1), LocalTime.parse("08:00")),
                Arguments.of(LocalDate.now(), LocalTime.MIDNIGHT),
                Arguments.of(LocalDate.now().plusDays(1), LocalTime.parse("08:00"))
        );
    }

    @ParameterizedTest
    @MethodSource("pastReservationDateTimes")
    void 사용자는_지난_날짜나_시간으로_예약_생성시_예외_발생(LocalDate date, LocalTime startAt) {
        // given
        Long timeId = 1L;
        Long themeId = 1L;
        ReservationTime time = new ReservationTime(timeId, startAt);
        when(reservationTimeRepository.findBy(timeId))
                .thenReturn(Optional.of(time));

        // when & then
        assertThatThrownBy(() -> service.createUserReservation("브라운", date, timeId, themeId))
                .isInstanceOf(PastReservationException.class)
                .hasMessage("이미 지난 시간으로는 예약할 수 없습니다.");

        verify(reservationTimeRepository).findBy(timeId);
        verifyNoInteractions(themeRepository);
        verifyNoInteractions(reservationRepository);
    }

    private static Stream<Arguments> pastReservationDateTimes() {
        return Stream.of(
                Arguments.of(LocalDate.now().minusDays(1), LocalTime.parse("08:00")),
                Arguments.of(LocalDate.now(), LocalTime.MIDNIGHT)
        );
    }

    @Test
    void 이미_예약된_시간이면_사용자_예약_생성시_예외_발생() {
        // given
        Long timeId = 1L;
        Long themeId = 1L;
        ReservationTime time = new ReservationTime(timeId, LocalTime.parse("08:00"));
        when(reservationTimeRepository.findBy(timeId))
                .thenReturn(Optional.of(time));
        when(reservationRepository.existWith(date, timeId, themeId))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> service.createUserReservation("브라운", date, timeId, themeId))
                .isInstanceOf(DuplicateReservationException.class)
                .hasMessage("이미 예약된 시간입니다.");

        verify(reservationTimeRepository).findBy(timeId);
        verify(reservationRepository).existWith(date, timeId, themeId);
        verifyNoInteractions(themeRepository);
        verify(reservationRepository, never()).insert(any(Reservation.class));
    }

    @Test
    void 존재하지_않는_timeId로_예약_생성시_예외_발생() {
        // given
        Long timeId = 999L;
        Long themeId = 1L;
        when(reservationRepository.existWith(date, timeId, themeId))
                .thenReturn(false);
        when(reservationTimeRepository.findBy(timeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.createUserReservation("홍길동", date, timeId, themeId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");

        verify(reservationTimeRepository).findBy(timeId);
        verify(reservationRepository, never()).existWith(any(LocalDate.class), anyLong(), anyLong());
        verifyNoInteractions(themeRepository);
        verify(reservationRepository, never()).insert(any(Reservation.class));
    }

    @Test
    void 존재하지_않는_themeId로_예약_생성시_예외_발생() {
        // given
        Long timeId = 1L;
        Long themeId = 999L;
        ReservationTime time = new ReservationTime(timeId, LocalTime.parse("08:00"));
        when(reservationRepository.existWith(date, timeId, themeId))
                .thenReturn(false);
        when(reservationTimeRepository.findBy(timeId))
                .thenReturn(Optional.of(time));
        when(themeRepository.findBy(themeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.createUserReservation("홍길동", date, timeId, themeId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 테마입니다.");

        verify(reservationTimeRepository).findBy(timeId);
        verify(reservationRepository).existWith(date, timeId, themeId);
        verify(themeRepository).findBy(themeId);
        verify(reservationRepository, never()).insert(any(Reservation.class));
    }

    @Test
    void 예약_삭제_테스트() {
        // given
        Long id = 1L;

        // when
        service.delete(id);

        // then
        verify(reservationRepository).delete(id);
    }

    @Test
    void 사용자_본인_예약_삭제_테스트() {
        // given
        Long id = 1L;
        String name = "브라운";
        Reservation reservation = new Reservation(
                id,
                name,
                date,
                new ReservationTime(1L, LocalTime.parse("08:00")),
                new Theme(1L, "테스트 테마", "테마 설명", "썸네일 주소"));
        when(reservationRepository.findById(id))
                .thenReturn(Optional.of(reservation));

        // when
        service.deleteUserReservation(id, name);

        // then
        verify(reservationRepository).findById(id);
        verify(reservationRepository).delete(id);
    }

    @Test
    void 존재하지_않는_예약_삭제시_예외_발생() {
        // given
        Long id = 999L;
        String name = "브라운";
        when(reservationRepository.findById(id))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.deleteUserReservation(id, name))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");

        verify(reservationRepository).findById(id);
        verify(reservationRepository, never()).delete(id);
    }

    @Test
    void 본인의_예약이_아니면_삭제시_예외_발생() {
        // given
        Long id = 1L;
        String name = "브라운";
        Reservation reservation = new Reservation(
                id,
                "구구",
                date,
                new ReservationTime(1L, LocalTime.parse("08:00")),
                new Theme(1L, "테스트 테마", "테마 설명", "썸네일 주소"));
        when(reservationRepository.findById(id))
                .thenReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> service.deleteUserReservation(id, name))
                .isInstanceOf(ForbiddenReservationException.class)
                .hasMessage("본인의 예약만 변경하거나 취소할 수 있습니다.");

        verify(reservationRepository).findById(id);
        verify(reservationRepository, never()).delete(id);
    }

    @Test
    void 사용자_본인_예약_변경_테스트() {
        // given
        Long id = 1L;
        String name = "브라운";
        Long timeId = 2L;
        LocalDate updateDate = date.plusDays(1);
        ReservationTime originalTime = new ReservationTime(1L, LocalTime.parse("08:00"));
        ReservationTime updateTime = new ReservationTime(timeId, LocalTime.parse("10:00"));
        Theme theme = new Theme(1L, "테스트 테마", "테마 설명", "썸네일 주소");
        Reservation reservation = new Reservation(id, name, date, originalTime, theme);
        Reservation updatedReservation = new Reservation(id, name, updateDate, updateTime, theme);
        when(reservationRepository.findById(id))
                .thenReturn(Optional.of(reservation), Optional.of(updatedReservation));
        when(reservationTimeRepository.findBy(timeId))
                .thenReturn(Optional.of(updateTime));

        // when
        Reservation result = service.updateUserReservation(id, name, updateDate, timeId);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(id),
                () -> assertThat(result.getDate()).isEqualTo(updateDate),
                () -> assertThat(result.getTime()).isEqualTo(updateTime),
                () -> assertThat(result.getTheme()).isEqualTo(theme));
        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository, times(2)).findById(id);
        verify(reservationTimeRepository).findBy(timeId);
        verify(reservationRepository).update(captor.capture());
        Reservation captured = captor.getValue();
        assertAll(
                () -> assertThat(captured.getId()).isEqualTo(id),
                () -> assertThat(captured.getName()).isEqualTo(name),
                () -> assertThat(captured.getDate()).isEqualTo(updateDate),
                () -> assertThat(captured.getTime()).isEqualTo(updateTime),
                () -> assertThat(captured.getTheme()).isEqualTo(theme));
    }

    @Test
    void 사용자_본인_예약_변경시_날짜만_변경한다() {
        // given
        Long id = 1L;
        String name = "브라운";
        LocalDate updateDate = date.plusDays(1);
        ReservationTime time = new ReservationTime(1L, LocalTime.parse("08:00"));
        Theme theme = new Theme(1L, "테스트 테마", "테마 설명", "썸네일 주소");
        Reservation reservation = new Reservation(id, name, date, time, theme);
        Reservation updatedReservation = new Reservation(id, name, updateDate, time, theme);
        when(reservationRepository.findById(id))
                .thenReturn(Optional.of(reservation), Optional.of(updatedReservation));

        // when
        Reservation result = service.updateUserReservation(id, name, updateDate, null);

        // then
        assertAll(
                () -> assertThat(result.getDate()).isEqualTo(updateDate),
                () -> assertThat(result.getTime()).isEqualTo(time));
        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verifyNoInteractions(reservationTimeRepository);
        verify(reservationRepository).update(captor.capture());
        Reservation captured = captor.getValue();
        assertAll(
                () -> assertThat(captured.getDate()).isEqualTo(updateDate),
                () -> assertThat(captured.getTime()).isEqualTo(time));
    }

    @Test
    void 사용자_본인_예약_변경시_시간만_변경한다() {
        // given
        Long id = 1L;
        String name = "브라운";
        Long timeId = 2L;
        ReservationTime originalTime = new ReservationTime(1L, LocalTime.parse("08:00"));
        ReservationTime updateTime = new ReservationTime(timeId, LocalTime.parse("10:00"));
        Theme theme = new Theme(1L, "테스트 테마", "테마 설명", "썸네일 주소");
        Reservation reservation = new Reservation(id, name, date, originalTime, theme);
        Reservation updatedReservation = new Reservation(id, name, date, updateTime, theme);
        when(reservationRepository.findById(id))
                .thenReturn(Optional.of(reservation), Optional.of(updatedReservation));
        when(reservationTimeRepository.findBy(timeId))
                .thenReturn(Optional.of(updateTime));

        // when
        Reservation result = service.updateUserReservation(id, name, null, timeId);

        // then
        assertAll(
                () -> assertThat(result.getDate()).isEqualTo(date),
                () -> assertThat(result.getTime()).isEqualTo(updateTime));
        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationTimeRepository).findBy(timeId);
        verify(reservationRepository).update(captor.capture());
        Reservation captured = captor.getValue();
        assertAll(
                () -> assertThat(captured.getDate()).isEqualTo(date),
                () -> assertThat(captured.getTime()).isEqualTo(updateTime));
    }

    @Test
    void 사용자_본인_예약_변경시_변경할_값이_없으면_예외_발생() {
        // given
        Long id = 1L;
        String name = "브라운";
        Reservation reservation = new Reservation(
                id,
                name,
                date,
                new ReservationTime(1L, LocalTime.parse("08:00")),
                new Theme(1L, "테스트 테마", "테마 설명", "썸네일 주소"));
        when(reservationRepository.findById(id))
                .thenReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> service.updateUserReservation(id, name, null, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("변경할 날짜 또는 시간이 필요합니다.");

        verify(reservationRepository).findById(id);
        verifyNoInteractions(reservationTimeRepository);
        verify(reservationRepository, never()).update(any(Reservation.class));
    }

    @Test
    void 존재하지_않는_예약_변경시_예외_발생() {
        // given
        Long id = 999L;
        String name = "브라운";
        Long timeId = 2L;
        LocalDate updateDate = date.plusDays(1);
        when(reservationRepository.findById(id))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.updateUserReservation(id, name, updateDate, timeId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약입니다.");

        verify(reservationRepository).findById(id);
        verifyNoInteractions(reservationTimeRepository);
        verify(reservationRepository, never()).update(any(Reservation.class));
    }

    @Test
    void 본인의_예약이_아니면_변경시_예외_발생() {
        // given
        Long id = 1L;
        String name = "브라운";
        Long timeId = 2L;
        LocalDate updateDate = date.plusDays(1);
        Reservation reservation = new Reservation(
                id,
                "구구",
                date,
                new ReservationTime(1L, LocalTime.parse("08:00")),
                new Theme(1L, "테스트 테마", "테마 설명", "썸네일 주소"));
        when(reservationRepository.findById(id))
                .thenReturn(Optional.of(reservation));

        // when & then
        assertThatThrownBy(() -> service.updateUserReservation(id, name, updateDate, timeId))
                .isInstanceOf(ForbiddenReservationException.class)
                .hasMessage("본인의 예약만 변경하거나 취소할 수 있습니다.");

        verify(reservationRepository).findById(id);
        verifyNoInteractions(reservationTimeRepository);
        verify(reservationRepository, never()).update(any(Reservation.class));
    }

    @Test
    void 존재하지_않는_시간으로_예약_변경시_예외_발생() {
        // given
        Long id = 1L;
        String name = "브라운";
        Long timeId = 999L;
        LocalDate updateDate = date.plusDays(1);
        Reservation reservation = new Reservation(
                id,
                name,
                date,
                new ReservationTime(1L, LocalTime.parse("08:00")),
                new Theme(1L, "테스트 테마", "테마 설명", "썸네일 주소"));
        when(reservationRepository.findById(id))
                .thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.findBy(timeId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.updateUserReservation(id, name, updateDate, timeId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 예약 시간입니다.");

        verify(reservationRepository).findById(id);
        verify(reservationTimeRepository).findBy(timeId);
        verify(reservationRepository, never()).update(any(Reservation.class));
    }

    @Test
    void 예약_가능한_시간_조회_테스트() {
        // given
        Long themeId = 1L;
        ReservationTime reservedTime = new ReservationTime(1L, LocalTime.parse("08:00"));
        ReservationTime availableTime = new ReservationTime(2L, LocalTime.parse("10:00"));
        Theme theme = new Theme(themeId, "테스트 테마", "테마 설명", "썸네일 주소");
        Reservation reservation = new Reservation(1L, "브라운", date, reservedTime, theme);

        when(reservationTimeRepository.findAll())
                .thenReturn(List.of(reservedTime, availableTime));
        when(reservationRepository.findReservationsByThemeAndDate(themeId, date))
                .thenReturn(List.of(reservation));

        // when
        List<TimeAvailabilityResult> result = service.findAvailableTime(themeId, date);

        // then
        assertThat(result).extracting(TimeAvailabilityResult::available)
                .containsExactly(false, true);
        verify(reservationTimeRepository).findAll();
        verify(reservationRepository).findReservationsByThemeAndDate(themeId, date);
    }
}
