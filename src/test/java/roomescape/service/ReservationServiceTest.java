package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.vo.MemberName;
import roomescape.domain.vo.ReservationLocalDate;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationUpdateRequest;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.theme.ThemeRepository;
import roomescape.repository.time.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private static final ReservationTime SAVED_TIME = new ReservationTime(1L, "12:00");
    private static final Theme SAVED_THEME = new Theme(3L, new ThemeName("name"), "description",
        ThemeImageUrl.defaultImageUrl());
    private static final MemberName NAME = new MemberName("name");
    private static final LocalDate TOMORROW = LocalDate.now().plusDays(1);


    @Mock
    private ThemeRepository themeRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ReservationTimeRepository timeRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation() {
        return new Reservation(
            4L,
            new MemberName("name"),
            new ReservationLocalDate(LocalDate.now().plusDays(1L)),
            SAVED_TIME,
            SAVED_THEME);
    }

    @Test
    void 예약을_추가한다() {
        // given
        Reservation expectedReservation = reservation();

        when(timeRepository.findById(anyLong()))
            .thenReturn(Optional.of(SAVED_TIME));
        when(themeRepository.findById(anyLong()))
            .thenReturn(Optional.of(SAVED_THEME));
        when(reservationRepository.existsByDateAndTimeIdAndThemeId(any(), anyLong(), anyLong()))
            .thenReturn(false);
        when(reservationRepository.createReservation(any()))
            .thenReturn(expectedReservation);

        // when
        Reservation reservation = reservationService.addReservation(requestDtoFrom(expectedReservation));

        // then
        assertThat(reservation.getId()).isEqualTo(expectedReservation.getId());
        assertThat(reservation.getName()).isEqualTo(expectedReservation.getName());
        assertThat(reservation.getDateValue()).isEqualTo(expectedReservation.getDateValue());
        assertThat(reservation.getTime().getStartAt()).isEqualTo(expectedReservation.getTime().getStartAt());
        assertThat(reservation.getTheme().getDescription()).isEqualTo(expectedReservation.getTheme().getDescription());
        assertThat(reservation.getTheme().getNameValue()).isEqualTo(expectedReservation.getTheme().getNameValue());
        assertThat(reservation.getTheme().getImageUrlValue()).isEqualTo(
            expectedReservation.getTheme().getImageUrlValue());

        verify(timeRepository, times(1)).findById(anyLong());
        verify(themeRepository, times(1)).findById(anyLong());
        verify(reservationRepository, times(1)).existsByDateAndTimeIdAndThemeId(any(), anyLong(), anyLong());
        verify(reservationRepository, times(1)).createReservation(any());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 이미_지난_날짜로_추가하면_예외가_발생한다() {
        // given
        ReservationRequest request = new ReservationRequest(
            "n",
            LocalDate.now().minusDays(1),
            1L,
            1L);

        // when & them
        assertThatThrownBy(() -> reservationService.addReservation(request))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.PAST_DATE_RESERVATION.getMessage());

        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 존재하지_않는_시간으로_추가하면_예외가_발생한다() {
        // given
        when(timeRepository.findById(anyLong()))
            .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(requestDtoFrom(reservation())))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.TIME_NOT_FOUND.getMessage());

        verify(timeRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 존재하지_않는_테마로_추가하면_예외가_발생한다() {
        // given
        when(timeRepository.findById(anyLong()))
            .thenReturn(Optional.of(SAVED_TIME));
        when(themeRepository.findById(anyLong()))
            .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(requestDtoFrom(reservation())))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.THEME_NOT_FOUND.getMessage());

        verify(timeRepository, times(1)).findById(anyLong());
        verify(themeRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 동일한_테마에서_이미_예약된_날짜와_시간으로_추가하면_예외가_발생한다() {
        // given
        when(timeRepository.findById(anyLong()))
            .thenReturn(Optional.of(SAVED_TIME));
        when(themeRepository.findById(anyLong()))
            .thenReturn(Optional.of(SAVED_THEME));
        when(reservationRepository.existsByDateAndTimeIdAndThemeId(any(), anyLong(), anyLong()))
            .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(requestDtoFrom(reservation())))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.DUPLICATED_RESERVATION.getMessage());

        verify(timeRepository, times(1)).findById(anyLong());
        verify(themeRepository, times(1)).findById(anyLong());
        verify(reservationRepository, times(1)).existsByDateAndTimeIdAndThemeId(any(), anyLong(), anyLong());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 본인의_예약을_삭제한다() {
        // given
        Reservation reservation = new Reservation(
            1L, new MemberName("n"),
            new ReservationLocalDate(LocalDate.now().plusDays(1)),
            new ReservationTime(1L, "12:00"),
            SAVED_THEME
        );
        when(reservationRepository.findById(anyLong()))
            .thenReturn(Optional.of(reservation));

        // when & then
        Long id = reservation.getId();
        String name = reservation.getName().value();
        assertThatCode(() -> reservationService.deleteReservation(id, name))
            .doesNotThrowAnyException();

        verify(reservationRepository, times(1)).findById(id);
        verify(reservationRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 없는_예약을_삭제하는_경우_예외가_발생한다() {
        // given
        when(reservationRepository.findById(anyLong()))
            .thenReturn(Optional.empty());

        Reservation reservation = reservation();

        // when & then
        Long id = reservation.getId();
        String name = reservation.getName().value();
        assertThatThrownBy(() -> reservationService.deleteReservation(id, name))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.RESERVATION_NOT_FOUND.getMessage());

        verify(reservationRepository, times(1)).findById(id);
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 다른_사람의_예약을_삭제하는_경우_예외가_발생한다() {
        // given
        Reservation reservation = new Reservation(
            1L,
            new MemberName("n"),
            new ReservationLocalDate(LocalDate.now().plusDays(1)),
            new ReservationTime(1L, "12:00"),
            new Theme(1L, new ThemeName("n"), "d", ThemeImageUrl.defaultImageUrl())
        );

        when(reservationRepository.findById(anyLong()))
            .thenReturn(Optional.of(reservation));

        Long id = reservation.getId();
        String otherName = "otherName";

        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservation(id, otherName))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.FORBIDDEN.getMessage());

        verify(reservationRepository, times(1)).findById(id);
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 현재시간_이전의_예약을_삭제하는_경우_예외가_발생한다() {
        // given
        Reservation reservation = new Reservation(
            1L,
            new MemberName("n"),
            new ReservationLocalDate(LocalDate.now().minusDays(1)),
            new ReservationTime(1L, "12:00"),
            new Theme(1L, new ThemeName("n"), "d", ThemeImageUrl.defaultImageUrl())
        );

        when(reservationRepository.findById(anyLong()))
            .thenReturn(Optional.of(reservation));

        // when & then
        Long id = reservation.getId();
        String name = reservation.getName().value();
        assertThatThrownBy(() -> reservationService.deleteReservation(id, name))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.PAST_RESERVATION_CANCEL.getMessage());

        verify(reservationRepository, times(1)).findById(id);
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 예약자_이름으로_된_예약_목록을_조회한다() {
        // given
        Reservation savedReservation1 = new Reservation(1L, NAME, new ReservationLocalDate(TOMORROW), SAVED_TIME,
            SAVED_THEME);
        Reservation savedReservation2 = new Reservation(2L, NAME, new ReservationLocalDate(TOMORROW.plusDays(1)),
            SAVED_TIME, SAVED_THEME);

        when(reservationRepository.findAllByMemberName(any()))
            .thenReturn(List.of(savedReservation1, savedReservation2));

        // when
        List<Reservation> reservations = reservationService.getReservations(NAME.value());

        // then
        assertThat(reservations).containsExactlyInAnyOrder(savedReservation1, savedReservation2);

        verify(reservationRepository, times(1)).findAllByMemberName(any());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 날짜와_테마아이디로_예약가능한_시간을_조회한다() {
        // given
        LocalDate date = LocalDate.now().minusDays(1L);
        ReservationTime availableTime2 = new ReservationTime(2L, "14:30");

        when(themeRepository.findById(anyLong()))
            .thenReturn(Optional.of(SAVED_THEME));
        when(timeRepository.findAvailableTimeByDateAndThemeId(any(), anyLong()))
            .thenReturn(List.of(SAVED_TIME, availableTime2));

        // when
        List<ReservationTime> availableTimes = reservationService
            .getAvailableTimes(date, SAVED_THEME.getId());

        // then
        assertThat(availableTimes).containsExactlyInAnyOrder(SAVED_TIME, availableTime2);

        verify(themeRepository, times(1)).findById(anyLong());
        verify(timeRepository, times(1)).findAvailableTimeByDateAndThemeId(any(), anyLong());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 날짜와_시간이_같더라도_테마가_다르면_예약할_수_있다() {
        // given
        Theme otherTheme = new Theme(2L, new ThemeName("name"), "description", ThemeImageUrl.defaultImageUrl());
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        Reservation reservation = new Reservation("name", tomorrow, SAVED_TIME, otherTheme);

        when(timeRepository.findById(anyLong()))
            .thenReturn(Optional.of(SAVED_TIME));
        when(themeRepository.findById(anyLong()))
            .thenReturn(Optional.of(otherTheme));
        when(reservationRepository.existsByDateAndTimeIdAndThemeId(any(), anyLong(), anyLong()))
            .thenReturn(false);

        // when & then
        assertThatCode(() -> reservationService.addReservation(requestDtoFrom(reservation)))
            .doesNotThrowAnyException();

        verify(timeRepository, times(1)).findById(anyLong());
        verify(themeRepository, times(1)).findById(anyLong());
        verify(reservationRepository, times(1)).existsByDateAndTimeIdAndThemeId(any(), anyLong(), anyLong());
        verify(reservationRepository, times(1)).createReservation(any());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 예약이_존재하는_시간을_삭제하는_경우_예외가_발생한다() {
        // given
        when(reservationRepository.existsByTimeIdAndDateOnOrAfter(anyLong(), any()))
            .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservationTime(SAVED_TIME.getId()))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.TIME_HAS_RESERVATIONS.getMessage());

        verify(reservationRepository, times(1)).existsByTimeIdAndDateOnOrAfter(anyLong(), any());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 사용자는_본인_예약의_날짜_및_시간을_변경할_수_있다() {
        //given
        Reservation previous = new Reservation(
            1L,
            NAME,
            new ReservationLocalDate(TOMORROW),
            SAVED_TIME,
            SAVED_THEME
        );
        Reservation updated = new Reservation(
            1L,
            NAME,
            new ReservationLocalDate(TOMORROW.plusDays(1)),
            new ReservationTime(2L, SAVED_TIME.getStartAt().plusHours(1)),
            SAVED_THEME
        );

        String name = previous.getName().value();
        ReservationUpdateRequest request = updateRequestOf(updated);

        when(reservationRepository.findById(anyLong()))
            .thenReturn(Optional.of(previous));
        when(timeRepository.findById(anyLong()))
            .thenReturn(Optional.of(updated.getTime()));
        when(reservationRepository.existsByDateAndTimeIdAndThemeId(any(), anyLong(), anyLong()))
            .thenReturn(false);

        //when
        assertThatCode(() -> reservationService.updateDateTime(updated.getId(), name, request))
            .doesNotThrowAnyException();

        //then
        verify(reservationRepository, times(1)).findById(previous.getId());
        verify(timeRepository, times(1)).findById(updated.getTimeId());
        verify(reservationRepository, times(1))
            .existsByDateAndTimeIdAndThemeId(request.date(), request.timeId(), previous.getThemeId());
        verify(reservationRepository, times(1)).updateById(previous.getId(), updated);
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 예약의_날짜를_오늘_포함_과거_날짜로_변경하는_경우_예외가_발생한다() {
        //given
        Reservation previous = new Reservation(
            1L,
            NAME,
            new ReservationLocalDate(TOMORROW),
            SAVED_TIME,
            SAVED_THEME
        );
        Reservation updated = new Reservation(
            1L,
            NAME,
            new ReservationLocalDate(TOMORROW.minusDays(1)),
            new ReservationTime(2L, SAVED_TIME.getStartAt().plusHours(1)),
            SAVED_THEME
        );

        String name = previous.getName().value();
        ReservationUpdateRequest request = updateRequestOf(updated);

        //when
        assertThatThrownBy(() -> reservationService.updateDateTime(updated.getId(), name, request))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.PAST_DATE_RESERVATION.getMessage());

        //then
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 존재하지_않는_예약을_변경하는_경우_예외가_발생한다() {
        //given
        Reservation previous = new Reservation(
            1L,
            NAME,
            new ReservationLocalDate(TOMORROW),
            SAVED_TIME,
            SAVED_THEME
        );
        Reservation updated = new Reservation(
            1L,
            NAME,
            new ReservationLocalDate(TOMORROW.plusDays(1)),
            new ReservationTime(2L, SAVED_TIME.getStartAt().plusHours(1)),
            SAVED_THEME
        );

        String name = previous.getName().value();
        ReservationUpdateRequest request = updateRequestOf(updated);

        when(reservationRepository.findById(anyLong()))
            .thenReturn(Optional.empty());

        long notFoundReservationId = 2L;

        //when
        assertThatThrownBy(() -> reservationService.updateDateTime(notFoundReservationId, name, request))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.RESERVATION_NOT_FOUND.getMessage());

        //then
        verify(reservationRepository, times(1)).findById(notFoundReservationId);
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 예약을_존재하지_않는_시간으로_변경하는_경우_예외가_발생한다() {
        //given
        Reservation previous = new Reservation(
            1L,
            NAME,
            new ReservationLocalDate(TOMORROW),
            SAVED_TIME,
            SAVED_THEME
        );
        Reservation updated = new Reservation(
            1L,
            NAME,
            new ReservationLocalDate(TOMORROW.plusDays(1)),
            new ReservationTime(2L, SAVED_TIME.getStartAt().plusHours(1)),
            SAVED_THEME
        );

        String name = previous.getName().value();
        ReservationUpdateRequest request = updateRequestOf(updated);

        when(reservationRepository.findById(anyLong()))
            .thenReturn(Optional.of(previous));
        when(timeRepository.findById(anyLong()))
            .thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> reservationService.updateDateTime(updated.getId(), name, request))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.TIME_NOT_FOUND.getMessage());

        //then
        verify(reservationRepository, times(1)).findById(previous.getId());
        verify(timeRepository, times(1)).findById(updated.getTimeId());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 중복되는_예약으로_변경하는_경우_예외가_발생한다() {
        //given
        Reservation previous = new Reservation(
            1L,
            NAME,
            new ReservationLocalDate(TOMORROW),
            SAVED_TIME,
            SAVED_THEME
        );
        Reservation updated = new Reservation(
            1L,
            NAME,
            new ReservationLocalDate(TOMORROW.plusDays(1)),
            new ReservationTime(2L, SAVED_TIME.getStartAt().plusHours(1)),
            SAVED_THEME
        );

        String name = previous.getName().value();
        ReservationUpdateRequest request = updateRequestOf(updated);

        when(reservationRepository.findById(anyLong()))
            .thenReturn(Optional.of(previous));
        when(timeRepository.findById(anyLong()))
            .thenReturn(Optional.of(previous.getTime()));
        when(reservationRepository.existsByDateAndTimeIdAndThemeId(any(), anyLong(), anyLong()))
            .thenReturn(true);

        //when
        assertThatThrownBy(() -> reservationService.updateDateTime(updated.getId(), name, request))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.DUPLICATED_RESERVATION.getMessage());

        //then
        verify(reservationRepository, times(1)).findById(previous.getId());
        verify(timeRepository, times(1)).findById(updated.getTimeId());
        verify(reservationRepository, times(1))
            .existsByDateAndTimeIdAndThemeId(request.date(), request.timeId(), previous.getThemeId());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 다른_사용자의_예약을_변경하는_경우_예외가_발생한다() {
        //given
        Reservation previous = new Reservation(
            1L,
            NAME,
            new ReservationLocalDate(TOMORROW),
            SAVED_TIME,
            SAVED_THEME
        );
        Reservation updated = new Reservation(
            1L,
            NAME,
            new ReservationLocalDate(TOMORROW.plusDays(1)),
            new ReservationTime(2L, SAVED_TIME.getStartAt().plusHours(1)),
            SAVED_THEME
        );

        String otherName = "otherName";
        ReservationUpdateRequest request = updateRequestOf(updated);

        when(reservationRepository.findById(anyLong()))
            .thenReturn(Optional.of(previous));
        when(timeRepository.findById(anyLong()))
            .thenReturn(Optional.of(previous.getTime()));
        when(reservationRepository.existsByDateAndTimeIdAndThemeId(any(), anyLong(), anyLong()))
            .thenReturn(false);

        //when
        assertThatThrownBy(() -> reservationService.updateDateTime(updated.getId(), otherName, request))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.FORBIDDEN.getMessage());

        //then
        verify(reservationRepository, times(1)).findById(previous.getId());
        verify(timeRepository, times(1)).findById(updated.getTimeId());
        verify(reservationRepository, times(1))
            .existsByDateAndTimeIdAndThemeId(request.date(), request.timeId(), previous.getThemeId());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }

    @Test
    void 지난_날짜의_예약을_변경하는_경우_예외가_발생한다() {
        //given
        Reservation previous = new Reservation(
            1L,
            NAME,
            new ReservationLocalDate(TOMORROW.minusDays(1)),
            SAVED_TIME,
            SAVED_THEME
        );
        Reservation updated = new Reservation(
            1L,
            NAME,
            new ReservationLocalDate(TOMORROW.plusDays(1)),
            new ReservationTime(2L, SAVED_TIME.getStartAt().plusHours(1)),
            SAVED_THEME
        );

        String name = previous.getName().value();
        ReservationUpdateRequest request = updateRequestOf(updated);

        when(reservationRepository.findById(anyLong()))
            .thenReturn(Optional.of(previous));
        when(timeRepository.findById(anyLong()))
            .thenReturn(Optional.of(previous.getTime()));
        when(reservationRepository.existsByDateAndTimeIdAndThemeId(any(), anyLong(), anyLong()))
            .thenReturn(false);

        //when
        assertThatThrownBy(() -> reservationService.updateDateTime(updated.getId(), name, request))
            .isInstanceOf(RoomEscapeException.class)
            .hasMessageContaining(ErrorCode.PAST_RESERVATION_UPDATE.getMessage());

        //then
        verify(reservationRepository, times(1)).findById(previous.getId());
        verify(timeRepository, times(1)).findById(updated.getTimeId());
        verify(reservationRepository, times(1))
            .existsByDateAndTimeIdAndThemeId(request.date(), request.timeId(), previous.getThemeId());
        verifyNoMoreInteractions(themeRepository, timeRepository, reservationRepository);
    }


    private ReservationRequest requestDtoFrom(Reservation reservation) {
        return new ReservationRequest(
            reservation.getName().value(),
            reservation.getDateValue(),
            reservation.getTime().getId(),
            reservation.getThemeId());
    }

    private ReservationUpdateRequest updateRequestOf(Reservation expectedReservation) {
        return new ReservationUpdateRequest(expectedReservation.getDateValue(), expectedReservation.getTimeId());
    }
}
