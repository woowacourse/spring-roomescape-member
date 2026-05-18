package roomescape.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static roomescape.exception.dto.ErrorCode.*;
import static roomescape.exception.dto.ErrorCode.NOT_FOUND_THEME;

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
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.ThemeWithCount;
import roomescape.domain.theme.Theme;
import roomescape.domain.reservationTime.ReservationTime;
import roomescape.domain.reservationTime.ReservationTimeCondition;
import roomescape.domain.reservationTime.ReservationTimeWithAvailable;
import roomescape.dto.reservation.AddReservationRequest;
import roomescape.dto.reservation.UpdateReservationRequest;
import roomescape.dto.theme.PopularConditionRequest;
import roomescape.exception.exception.DuplicatedResourceException;
import roomescape.exception.exception.InvalidRequestException;
import roomescape.exception.exception.NotFoundResourceException;
import roomescape.repository.theme.ThemeRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationTime.ReservationTimeRepository;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("예약 생성 시 유효한 시간 ID, 테마 ID인 경우 정상 작동 테스트")
    void addReservationTest() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("10:00"));
        Theme theme = new Theme(1L, "name", "description", "image");
        LocalDate futureDate = LocalDate.now().plusDays(1);

        when(reservationTimeRepository.getReservationTime(anyLong())).thenReturn(Optional.of(reservationTime));
        when(themeRepository.getTheme(anyLong())).thenReturn(Optional.of(theme));
        when(reservationRepository.existsByTimeIdAndThemeIdAndDate(anyLong(), anyLong(), any())).thenReturn(false);
        when(reservationRepository.addReservation(any())).thenReturn(new Reservation(1L, "브라운", futureDate, reservationTime, theme));

        Reservation reservation = reservationService.addReservation(new AddReservationRequest("브라운", futureDate, 1L, 1L));

        assertThat(reservation)
                .usingRecursiveComparison()
                .isEqualTo(new Reservation(1L, "브라운", futureDate, reservationTime, theme));
    }

    @Test
    @DisplayName("예약 생성 시 지나간 날짜인 경우 예외 테스트")
    void addReservationFailByPastDateTest() {
        ReservationTime reservationTime = new ReservationTime(1L, LocalTime.parse("10:00"));

        assertThatThrownBy(() -> reservationService.addReservation(
                new AddReservationRequest("브라운", LocalDate.now().minusDays(1), 1L, 1L)))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage(INVALID_RESERVATION_DATE.getMessage());
    }

    @Test
    @DisplayName("오늘 날짜에서 지난 시간 예약 시 예외 발생 테스트")
    void addReservationFailByPastTimeTest() {
        ReservationTime pastTime = new ReservationTime(1L, LocalTime.of(0, 1));
        when(reservationTimeRepository.getReservationTime(anyLong())).thenReturn(Optional.of(pastTime));

        assertThatThrownBy(() -> reservationService.addReservation(
                new AddReservationRequest("브라운", LocalDate.now(), 1L, 1L)))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage(INVALID_RESERVATION_TIME.getMessage());
    }

    @Test
    @DisplayName("예약 생성 시 존재하지 않는 시간ID인 경우 예외 테스트")
    void addReservationFailByInvalidTimeIdTest() {
        when(reservationTimeRepository.getReservationTime(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.addReservation(
                new AddReservationRequest("브라운", LocalDate.now().plusDays(1), 1L, 1L)))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage(NOT_FOUND_RESERVATION_TIME.getMessage());
    }

    @Test
    @DisplayName("예약 생성 시 존재하지 않는 테마 ID인 경우 예외 테스트")
    void addReservationFailByInvalidThemeIdTest() {
        when(reservationTimeRepository.getReservationTime(anyLong())).thenReturn(Optional.of(new ReservationTime(1L, LocalTime.parse("10:00"))));
        when(themeRepository.getTheme(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.addReservation(
                new AddReservationRequest("브라운", LocalDate.now().plusDays(1), 1L, 1L)))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage(NOT_FOUND_THEME.getMessage());
    }

    @Test
    @DisplayName("같은 시간, 날짜, themeId가 존재하는 경우 예약 생성 시 예외 테스트")
    void addReservationFailByDuplicatedTimeAndDateAndTheme() {
        when(reservationTimeRepository.getReservationTime(anyLong())).thenReturn(Optional.of(new ReservationTime(1L, LocalTime.parse("10:00"))));
        when(themeRepository.getTheme(anyLong())).thenReturn(Optional.of(new Theme(1L, "name", "description", "image")));
        when(reservationRepository.existsByTimeIdAndThemeIdAndDate(anyLong(), anyLong(), any())).thenReturn(true);

        assertThatThrownBy(() -> reservationService.addReservation(
                new AddReservationRequest("브라운", LocalDate.now().plusDays(1), 1L, 1L)))
                .isExactlyInstanceOf(DuplicatedResourceException.class)
                .hasMessage(DUPLICATED_RESERVATION.getMessage());
    }

    @Test
    @DisplayName("이름으로 삭제할 경우 존재하지 않는 예약 id 입력 시 예외 테스트")
    void deleteReservationByNameFailByNotFoundTest() {
        when(reservationRepository.getReservationById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.deleteReservationByName(1L, "브라운"))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage(NOT_FOUND_RESERVATION.getMessage());
    }

    @Test
    @DisplayName("이름으로 삭제할 경우 이름 불일치 시 삭제 예외 테스트")
    void deleteReservationByNameFailByNameMismatchTest() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new Theme(1L, "name", "description", "image"));
        when(reservationRepository.getReservationById(anyLong())).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.deleteReservationByName(1L, "다른이름"))
                .isExactlyInstanceOf(InvalidRequestException.class)
                .hasMessage(UNAUTHORIZED_RESERVATION_ACCESS.getMessage());
    }

    @Test
    @DisplayName("정상적으로 예약 삭제 테스트")
    void deleteReservationByNameTest() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new Theme(1L, "name", "description", "image"));
        when(reservationRepository.getReservationById(anyLong())).thenReturn(Optional.of(reservation));

        assertThatCode(() -> reservationService.deleteReservationByName(1L, "브라운"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("수정 시 존재하지 않는 예약 id인 경우 예외 테스트")
    void updateReservationFailByNotFoundTest() {
        when(reservationRepository.getReservationById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.updateReservation(1L,
                new UpdateReservationRequest("브라운", LocalDate.now().plusDays(1), 1L)))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage(NOT_FOUND_RESERVATION.getMessage());
    }

    @Test
    @DisplayName("수정 시 이름 불일치인 경우 예외 테스트")
    void updateReservationFailByNameMismatchTest() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new Theme(1L, "name", "description", "image"));
        when(reservationRepository.getReservationById(anyLong())).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.updateReservation(1L,
                new UpdateReservationRequest("다른이름", LocalDate.now().plusDays(1), 1L)))
                .isExactlyInstanceOf(InvalidRequestException.class)
                .hasMessage(UNAUTHORIZED_RESERVATION_ACCESS.getMessage());
    }

    @Test
    @DisplayName("수정 시 존재하지 않는 timeId인 경우 예외 테스트")
    void updateReservationFailByInvalidTimeIdTest() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new Theme(1L, "name", "description", "image"));
        when(reservationRepository.getReservationById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.getReservationTime(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reservationService.updateReservation(1L,
                new UpdateReservationRequest("브라운", LocalDate.now().plusDays(1), 999L)))
                .isExactlyInstanceOf(NotFoundResourceException.class)
                .hasMessage(NOT_FOUND_RESERVATION_TIME.getMessage());
    }

    @Test
    @DisplayName("수정 시 지난 날짜인 경우 예외 테스트")
    void updateReservationFailByPastDateTest() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new Theme(1L, "name", "description", "image"));
        when(reservationRepository.getReservationById(anyLong())).thenReturn(Optional.of(reservation));

        assertThatThrownBy(() -> reservationService.updateReservation(1L,
                new UpdateReservationRequest("브라운", LocalDate.now().minusDays(1), 1L)))
                .isExactlyInstanceOf(InvalidRequestException.class)
                .hasMessage(INVALID_RESERVATION_DATE.getMessage());
    }

    @Test
    @DisplayName("수정 시 오늘 날짜 + 지난 시간인 경우 예외 테스트")
    void updateReservationFailByPastTimeTest() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new Theme(1L, "name", "description", "image"));
        when(reservationRepository.getReservationById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.getReservationTime(anyLong())).thenReturn(Optional.of(new ReservationTime(1L, LocalTime.of(0, 1))));

        assertThatThrownBy(() -> reservationService.updateReservation(1L,
                new UpdateReservationRequest("브라운", LocalDate.now(), 1L)))
                .isExactlyInstanceOf(InvalidRequestException.class)
                .hasMessage(INVALID_RESERVATION_TIME.getMessage());
    }

    @Test
    @DisplayName("수정 시 이미 해당 테마와 날짜와 시간에 예약이 존재하는 경우 예외 테스트")
    void updateReservationFailByDuplicatedTest() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new Theme(1L, "name", "description", "image"));
        when(reservationRepository.getReservationById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.getReservationTime(anyLong())).thenReturn(Optional.of(new ReservationTime(1L, LocalTime.parse("10:00"))));
        when(reservationRepository.existsByTimeIdAndThemeIdAndDate(anyLong(), anyLong(), any())).thenReturn(true);

        assertThatThrownBy(() -> reservationService.updateReservation(1L,
                new UpdateReservationRequest("브라운", LocalDate.now().plusDays(1), 1L)))
                .isExactlyInstanceOf(DuplicatedResourceException.class)
                .hasMessage(DUPLICATED_RESERVATION.getMessage());
    }

    @Test
    @DisplayName("정상적으로 예약 수정 테스트")
    void updateReservationTest() {
        Reservation reservation = new Reservation(1L, "브라운", LocalDate.now().plusDays(1),
                new ReservationTime(1L, LocalTime.parse("10:00")),
                new Theme(1L, "name", "description", "image"));
        when(reservationRepository.getReservationById(anyLong())).thenReturn(Optional.of(reservation));
        when(reservationTimeRepository.getReservationTime(anyLong())).thenReturn(Optional.of(new ReservationTime(2L, LocalTime.parse("11:00"))));
        when(reservationRepository.existsByTimeIdAndThemeIdAndDate(anyLong(), anyLong(), any())).thenReturn(false);
        when(reservationRepository.updateReservation(anyLong(), any(), anyLong())).thenReturn(reservation);
        when(reservationRepository.getReservationById(anyLong())).thenReturn(Optional.of(reservation));

        assertThatCode(() -> reservationService.updateReservation(1L,
                new UpdateReservationRequest("브라운", LocalDate.now().plusDays(2), 2L)))
                .doesNotThrowAnyException();
    }
}
