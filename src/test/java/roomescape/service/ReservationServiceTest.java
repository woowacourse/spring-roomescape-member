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
import roomescape.dto.SaveReservationTimeRequest;
import roomescape.dto.SaveThemeRequest;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

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
        given(reservationRepository.deleteById(1L)).willReturn(1);

        // When & Then
        assertThatCode(() -> reservationService.deleteReservation(1L))
                .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 예약 정보를 삭제하려고 하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenDeleteNotExistReservationTest() {
        // Given
        given(reservationRepository.deleteById(1L)).willReturn(0);

        // When & Then
        assertThatThrownBy(() -> reservationService.deleteReservation(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 id의 예약이 존재하지 않습니다.");
    }

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
        final List<ReservationTime> reservationTimes = reservationService.getReservationTimes();

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
        final ReservationTime reservationTime = reservationService.saveReservationTime(saveReservationTimeRequest);

        // Then
        assertThat(reservationTime).isEqualTo(savedReservationTime);
    }

    @DisplayName("예약 시간 정보를 삭제한다.")
    @Test
    void deleteReservationTimeTest() {
        // Given
        given(reservationTimeRepository.deleteById(1L)).willReturn(1);

        // When & Then
        assertThatCode(() -> reservationService.deleteReservationTime(1L))
                .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 예약 시간 정보를 삭제하려고 하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenDeleteNotExistReservationTimeTest() {
        // Given
        given(reservationTimeRepository.deleteById(1L)).willReturn(0);

        // When & Then
        assertThatThrownBy(() -> reservationService.deleteReservationTime(1L))
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
        assertThatThrownBy(() -> reservationService.saveReservationTime(saveReservationTimeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 예약시간이 있습니다.");
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

    @DisplayName("해당 시간을 참조하고 있는 예약이 하나라도 있으면 삭제시 예외가 발생한다.")
    @Test
    void throwExceptionWhenDeleteReservationTimeHasRelation() {
        // Given
        final Long reservationTimeId = 1L;
        given(reservationRepository.existByTimeId(reservationTimeId)).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> reservationService.deleteReservationTime(reservationTimeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약에 포함된 시간 정보는 삭제할 수 없습니다.");
    }

    @DisplayName("전체 테마 정보를 조회한다.")
    @Test
    void getThemesTest() {
        // Given
        final List<Theme> themes = List.of(
                Theme.of(1L, "켈리의 하루", "켈켈켈", "켈리 사진"),
                Theme.of(2L, "테바의 하루", "테테테", "테바 사진")
        );

        given(themeRepository.findAll()).willReturn(themes);

        // When
        final List<Theme> findThemes = reservationService.getThemes();

        // Then
        assertThat(findThemes).hasSize(2);
    }

    @DisplayName("예약 시간 정보를 저장한다.")
    @Test
    void saveThemeTest() {
        // Given
        final String name = "켈리의 하루";
        final String description = "켈켈켈";
        final String thumbnail = "켈리 사진";
        final SaveThemeRequest saveThemeRequest = new SaveThemeRequest(name, description, thumbnail);
        final Theme savedTheme = Theme.of(1L, name, description, thumbnail);

        given(themeRepository.save(saveThemeRequest.toTheme())).willReturn(savedTheme);

        // When
        final Theme theme = reservationService.saveTheme(saveThemeRequest);

        // Then
        assertThat(theme.getId()).isEqualTo(1L);
    }

    @DisplayName("테마 정보를 삭제한다.")
    @Test
    void deleteThemeTest() {
        // Given
        given(themeRepository.deleteById(1L)).willReturn(1);

        // When & Then
        assertThatCode(() -> reservationService.deleteTheme(1L))
                .doesNotThrowAnyException();
    }
}
