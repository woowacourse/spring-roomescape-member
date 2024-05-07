package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.SaveReservationRequest;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private ReservationTimeRepository reservationTimeRepository;
    @Mock
    private ThemeRepository themeRepository;

    @DisplayName("전체 예약 정보를 조회한다.")
    @Test
    void getReservationsTest() {
        // Given
        final ReservationTime savedReservationTime1 = new ReservationTime(1L, LocalTime.now().plusHours(3));
        final ReservationTime savedReservationTime2 = new ReservationTime(2L, LocalTime.now().plusHours(4));
        final Theme theme = Theme.of(1L, "테바의 비밀친구", "테바의 은밀한 비밀친구", "대충 테바 사진 링크");
        final List<Reservation> savedReservations = List.of(
                Reservation.of(1L, "켈리", LocalDate.now().plusDays(5), savedReservationTime1, theme),
                Reservation.of(2L, "켈리", LocalDate.now().plusDays(6), savedReservationTime2, theme)
        );

        given(reservationRepository.findAll()).willReturn(savedReservations);

        // When
        final List<Reservation> reservations = reservationService.getReservations();

        // Then
        assertThat(reservations).hasSize(savedReservations.size());
    }

    @DisplayName("예약 정보를 저장한다.")
    @Test
    void saveReservationTest() {
        // Given
        final ReservationTime savedReservationTime = new ReservationTime(1L, LocalTime.now().plusHours(3));
        final Theme savedTheme = Theme.of(1L, "테바의 비밀친구", "테바의 은밀한 비밀친구", "대충 테바 사진 링크");
        final Reservation savedReservation = Reservation.of(1L, "켈리", LocalDate.now().plusDays(5), savedReservationTime, savedTheme);
        final SaveReservationRequest saveReservationRequest = new SaveReservationRequest(LocalDate.now().plusDays(5), "켈리", 1L, 1L);

        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(savedReservationTime));
        given(themeRepository.findById(1L)).willReturn(Optional.of(savedTheme));
        given(reservationRepository.save(saveReservationRequest.toReservation(savedReservationTime, savedTheme))).willReturn(savedReservation);

        // When
        final Reservation reservation = reservationService.saveReservation(saveReservationRequest);

        // Then
        assertThat(reservation).isEqualTo(savedReservation);
    }

    @DisplayName("저장하려는 예약 시간이 존재하지 않는다면 예외를 발생시킨다.")
    @Test
    void throwExceptionWhenSaveReservationWithNotExistReservationTimeTest() {
        // Given
        final SaveReservationRequest saveReservationRequest = new SaveReservationRequest(LocalDate.now(), "켈리", 1L, 1L);

        given(reservationTimeRepository.findById(1L)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reservationService.saveReservation(saveReservationRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 id의 예약 시간이 존재하지 않습니다.");
    }

    @DisplayName("예약 정보를 삭제한다.")
    @Test
    void deleteReservationTest() {
        // Given
        final ReservationTime savedReservationTime = new ReservationTime(1L, LocalTime.now().plusHours(3));
        final Theme savedTheme = Theme.of(1L, "테바의 비밀친구", "테바의 은밀한 비밀친구", "대충 테바 사진 링크");
        final Reservation savedReservation = Reservation.of(1L, "켈리", LocalDate.now().plusDays(5), savedReservationTime, savedTheme);

        given(reservationRepository.findById(1L)).willReturn(Optional.of(savedReservation));
        willDoNothing().given(reservationRepository).deleteById(1L);

        // When & Then
        assertThatCode(() -> reservationService.deleteReservation(1L))
                .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 예약 정보를 삭제하려고 하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenDeleteNotExistReservationTest() {
        // Given
        given(reservationRepository.findById(1L)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reservationService.deleteReservation(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 id의 예약이 존재하지 않습니다.");
    }

    @DisplayName("이미 존재하는 예약 날짜/시간/테마가 입력되면 예외가 발생한다.")
    @Test
    void throwExceptionWhenInputDuplicateReservationDate() {
        // Given
        final long timeId = 4L;
        final ReservationTime savedReservationTime = new ReservationTime(4L, LocalTime.now().plusHours(3));
        final long themeId = 9L;
        final Theme savedTheme = Theme.of(9L, "테바의 비밀친구", "테바의 은밀한 비밀친구", "대충 테바 사진 링크");
        final LocalDate date = LocalDate.now().plusDays(2);
        final String name = "테바";
        final SaveReservationRequest saveReservationRequest = new SaveReservationRequest(
                date,
                name,
                timeId,
                themeId
        );

        given(reservationTimeRepository.findById(timeId)).willReturn(Optional.of(savedReservationTime));
        given(themeRepository.findById(themeId)).willReturn(Optional.of(savedTheme));
        given(reservationRepository.existByDateAndTimeIdAndThemeId(date, timeId, themeId)).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> reservationService.saveReservation(saveReservationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 해당 날짜/시간의 테마 예약이 있습니다.");
    }
}
