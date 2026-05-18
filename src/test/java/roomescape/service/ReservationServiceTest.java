package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.command.ReservationSaveCommand;
import roomescape.service.command.ReservationUpdateCommand;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private static final long TIME_ID = 1L;
    private static final long THEME_ID = 1L;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("예약을 저장하면 id가 포함된 예약을 반환한다")
    void saveReservation() {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
        Theme theme = new Theme(THEME_ID, "우주 정거장", "설명", "https://example.com/1.jpg");
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationSaveCommand saveCommand = new ReservationSaveCommand("브라운", date, TIME_ID, THEME_ID);
        Reservation persisted = new Reservation(99L, "브라운", date, time, theme);

        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.of(time));
        given(themeRepository.findById(THEME_ID)).willReturn(Optional.of(theme));
        given(reservationRepository.addReservation(any(Reservation.class))).willReturn(persisted);

        Reservation saved = reservationService.saveReservation(saveCommand);

        assertThat(saved.id()).isEqualTo(99L);
        assertThat(saved.name()).isEqualTo("브라운");
        assertThat(saved.date()).isEqualTo(date);
        assertThat(saved.time().id()).isEqualTo(TIME_ID);
        assertThat(saved.theme().id()).isEqualTo(THEME_ID);
    }

    @Test
    @DisplayName("존재하지 않는 시간으로 예약하면 예외가 발생한다")
    void throwException_WhenTimeNotFound() {
        ReservationSaveCommand saveCommand = new ReservationSaveCommand("브라운", LocalDate.now().plusDays(1), TIME_ID,
                THEME_ID);
        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.saveReservation(saveCommand))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_TIME_NOT_FOUND));
    }

    @Test
    @DisplayName("존재하지 않는 테마로 예약하면 예외가 발생한다")
    void throwException_WhenThemeNotFound() {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
        ReservationSaveCommand saveCommand = new ReservationSaveCommand("브라운", LocalDate.now().plusDays(1), TIME_ID,
                THEME_ID);
        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.of(time));
        given(themeRepository.findById(THEME_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.saveReservation(saveCommand))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.THEME_NOT_FOUND));
    }

    @Test
    @DisplayName("사용자명에 해당하는 예약이 없으면 예외가 발생한다")
    void throwException_WhenNoReservationsByName() {
        given(reservationRepository.findReservationsByName(any())).willReturn(List.of());

        assertThatThrownBy(() -> reservationService.findReservationsByName(" "))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_NOT_FOUND));
    }

    @Test
    @DisplayName("사용자명이 null이면 예외가 발생한다")
    void throwException_WhenReservationNameIsNull() {
        assertThatThrownBy(() -> reservationService.findReservationsByName(null))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_INPUT));
    }

    @Test
    @DisplayName("관리자는 id로 예약을 삭제한다")
    void deleteReservationByAdmin() {
        long reservationId = 1L;
        Reservation reservation = createReservation(reservationId, "브라운", LocalDate.now().plusDays(1));
        given(reservationRepository.findById(reservationId)).willReturn(Optional.of(reservation));

        assertThatCode(() -> reservationService.deleteReservationByAdmin(reservationId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("관리자는 존재하지 않는 예약을 삭제할 수 없다")
    void throwException_WhenAdminDeleteReservationNotFound() {
        long reservationId = 1L;
        given(reservationRepository.findById(reservationId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.deleteReservationByAdmin(reservationId))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_NOT_FOUND));
    }

    @Test
    @DisplayName("사용자는 예약 목록에서 선택한 예약을 취소할 수 있다")
    void cancelReservation() {
        long reservationId = 1L;
        Reservation reservation = createReservation(reservationId, "브라운", LocalDate.now().plusDays(1));
        given(reservationRepository.findById(reservationId)).willReturn(Optional.of(reservation));

        assertThatCode(() -> reservationService.cancelReservation(reservationId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하지 않는 예약은 취소할 수 없다")
    void throwException_WhenCancelReservationNotFound() {
        long reservationId = 1L;
        given(reservationRepository.findById(reservationId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.cancelReservation(reservationId))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_NOT_FOUND));
    }

    @Test
    @DisplayName("지난 예약은 취소할 수 없다")
    void throwException_WhenCancelPastReservation() {
        long reservationId = 1L;
        Reservation reservation = createReservation(reservationId, "브라운", LocalDate.now().minusDays(1));
        given(reservationRepository.findById(reservationId)).willReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.cancelReservation(reservationId))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_ALREADY_PAST));
    }

    @Test
    @DisplayName("지난 날짜에 대한 예약 생성 시 예외가 발생한다")
    void throwException_WhenPastDate() {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
        ReservationSaveCommand saveCommand = new ReservationSaveCommand(
                "브라운",
                LocalDate.now().minusDays(1),
                TIME_ID,
                THEME_ID
        );

        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.of(time));

        assertThatThrownBy(() -> reservationService.saveReservation(saveCommand))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_PAST_TIME));
    }

    @Test
    @DisplayName("오늘의 지난 시간에 대한 예약 생성 시 예외가 발생한다")
    void throwException_WhenPastTimeToday() {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.now().minusMinutes(1));
        ReservationSaveCommand saveCommand = new ReservationSaveCommand(
                "브라운",
                LocalDate.now(),
                TIME_ID,
                THEME_ID
        );

        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.of(time));

        assertThatThrownBy(() -> reservationService.saveReservation(saveCommand))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_PAST_TIME));
    }

    @Test
    @DisplayName("같은 날짜, 시간, 테마의 예약이 이미 존재하면 예외를 던진다")
    void throwException_WhenSameDateTimeAndThemeReservationExists() {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
        Theme theme = new Theme(THEME_ID, "우주 정거장", "설명", "https://example.com/1.jpg");
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationSaveCommand saveCommand = new ReservationSaveCommand(
                "브라운",
                date,
                TIME_ID,
                THEME_ID
        );

        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.of(time));
        given(themeRepository.findById(THEME_ID)).willReturn(Optional.of(theme));
        given(reservationRepository.existsByDateAndTimeIdAndThemeId(date, TIME_ID, THEME_ID))
                .willReturn(true);

        assertThatThrownBy(() -> reservationService.saveReservation(saveCommand))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_DUPLICATED));
    }


    @Test
    @DisplayName("중복 예약이 아니면 예약을 생성한다")
    void saveReservation_WhenReservationDoesNotExist() {
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
        Theme theme = new Theme(THEME_ID, "우주 정거장", "설명", "https://example.com/1.jpg");
        ReservationSaveCommand saveCommand = new ReservationSaveCommand(
                "브라운",
                date,
                TIME_ID,
                THEME_ID
        );
        Reservation savedReseravtion = new Reservation(99L, "브라운", date, time, theme);

        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.of(time));
        given(themeRepository.findById(THEME_ID)).willReturn(Optional.of(theme));
        given(reservationRepository.existsByDateAndTimeIdAndThemeId(date, TIME_ID, THEME_ID))
                .willReturn(false);
        given(reservationRepository.addReservation(any(Reservation.class))).willReturn(savedReseravtion);

        Reservation saved = reservationService.saveReservation(saveCommand);

        assertThat(saved).isEqualTo(savedReseravtion);
    }

    @Test
    @DisplayName("사용자는 예약 날짜와 시간을 변경할 수 있다")
    void changeReservationDateTime() {
        long reservationId = 1L;
        LocalDate changedDate = LocalDate.now().plusDays(2);
        Reservation reservation = createReservation(reservationId, "브라운", LocalDate.now().plusDays(1));
        ReservationTime changedTime = new ReservationTime(2L, LocalTime.of(11, 0));
        ReservationUpdateCommand updateCommand = new ReservationUpdateCommand(changedDate, 2L);

        given(reservationRepository.findById(reservationId)).willReturn(Optional.of(reservation));
        given(reservationTimeRepository.findById(2L)).willReturn(Optional.of(changedTime));
        given(reservationRepository.existsConflictingReservation(changedDate, 2L, THEME_ID, reservationId))
                .willReturn(false);

        Reservation changedReservation = reservationService.changeReservationDateTime(reservationId, updateCommand);

        assertThat(changedReservation.id()).isEqualTo(reservationId);
        assertThat(changedReservation.name()).isEqualTo("브라운");
        assertThat(changedReservation.date()).isEqualTo(changedDate);
        assertThat(changedReservation.time()).isEqualTo(changedTime);
        assertThat(changedReservation.theme()).isEqualTo(reservation.theme());
    }

    @Test
    @DisplayName("존재하지 않는 예약은 변경할 수 없다")
    void throwException_WhenChangeReservationNotFound() {
        long reservationId = 1L;
        ReservationUpdateCommand updateCommand = new ReservationUpdateCommand(LocalDate.now().plusDays(1), TIME_ID);
        given(reservationRepository.findById(reservationId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.changeReservationDateTime(reservationId, updateCommand))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_NOT_FOUND));
    }

    @Test
    @DisplayName("지난 예약은 변경할 수 없다")
    void throwException_WhenChangePastReservation() {
        long reservationId = 1L;
        Reservation reservation = createReservation(reservationId, "브라운", LocalDate.now().minusDays(1));
        ReservationUpdateCommand updateCommand = new ReservationUpdateCommand(LocalDate.now().plusDays(1), TIME_ID);
        given(reservationRepository.findById(reservationId)).willReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.changeReservationDateTime(reservationId, updateCommand))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_ALREADY_PAST));
    }

    @Test
    @DisplayName("존재하지 않는 시간으로 예약을 변경할 수 없다")
    void throwException_WhenChangeReservationTimeNotFound() {
        long reservationId = 1L;
        Reservation reservation = createReservation(reservationId, "브라운", LocalDate.now().plusDays(1));
        ReservationUpdateCommand updateCommand = new ReservationUpdateCommand(LocalDate.now().plusDays(2), 999L);

        given(reservationRepository.findById(reservationId)).willReturn(Optional.of(reservation));
        given(reservationTimeRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.changeReservationDateTime(reservationId, updateCommand))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_TIME_NOT_FOUND));
    }

    @Test
    @DisplayName("지난 날짜와 시간으로 예약을 변경할 수 없다")
    void throwException_WhenChangeReservationToPastDateTime() {
        long reservationId = 1L;
        Reservation reservation = createReservation(reservationId, "브라운", LocalDate.now().plusDays(1));
        ReservationTime pastTime = new ReservationTime(TIME_ID, LocalTime.now().minusMinutes(1));
        ReservationUpdateCommand updateCommand = new ReservationUpdateCommand(LocalDate.now(), TIME_ID);

        given(reservationRepository.findById(reservationId)).willReturn(Optional.of(reservation));
        given(reservationTimeRepository.findById(TIME_ID)).willReturn(Optional.of(pastTime));

        assertThatThrownBy(() -> reservationService.changeReservationDateTime(reservationId, updateCommand))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_PAST_TIME));
    }

    @Test
    @DisplayName("변경하려는 날짜, 시간, 테마의 예약이 이미 있으면 변경할 수 없다")
    void throwException_WhenChangeReservationDuplicated() {
        long reservationId = 1L;
        LocalDate changedDate = LocalDate.now().plusDays(2);
        Reservation reservation = createReservation(reservationId, "브라운", LocalDate.now().plusDays(1));
        ReservationTime changedTime = new ReservationTime(2L, LocalTime.of(11, 0));
        ReservationUpdateCommand updateCommand = new ReservationUpdateCommand(changedDate, 2L);

        given(reservationRepository.findById(reservationId)).willReturn(Optional.of(reservation));
        given(reservationTimeRepository.findById(2L)).willReturn(Optional.of(changedTime));
        given(reservationRepository.existsConflictingReservation(changedDate, 2L, THEME_ID, reservationId))
                .willReturn(true);

        assertThatThrownBy(() -> reservationService.changeReservationDateTime(reservationId, updateCommand))
                .isInstanceOfSatisfying(RoomescapeException.class, exception ->
                        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.RESERVATION_DUPLICATED));
    }

    private Reservation createReservation(Long id, String name, LocalDate date) {
        ReservationTime time = new ReservationTime(TIME_ID, LocalTime.of(10, 0));
        Theme theme = new Theme(THEME_ID, "우주 정거장", "설명", "https://example.com/1.jpg");

        return new Reservation(id, name, date, time, theme);
    }
}
