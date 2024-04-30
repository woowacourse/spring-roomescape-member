package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.ClientName;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.ThemeDescription;
import roomescape.domain.ThemeName;
import roomescape.dto.SaveReservationRequest;
import roomescape.dto.SaveReservationTimeRequest;
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
        ReservationTime savedReservationTime1 = new ReservationTime(1L, LocalTime.now().plusHours(3));
        ReservationTime savedReservationTime2 = new ReservationTime(2L, LocalTime.now().plusHours(4));
        Theme theme = new Theme(1L, new ThemeName("테바의 비밀친구"), new ThemeDescription("테바의 은밀한 비밀친구"), "대충 테바 사진 링크");
        List<Reservation> savedReservations = List.of(
                new Reservation(1L, new ClientName("켈리"), LocalDate.now().plusDays(5), savedReservationTime1, theme),
                new Reservation(2L, new ClientName("켈리"), LocalDate.now().plusDays(6), savedReservationTime2, theme)
        );

        given(reservationRepository.findAll()).willReturn(savedReservations);

        // When
        List<Reservation> reservations = reservationService.getReservations();

        // Then
        assertThat(reservations).hasSize(savedReservations.size());
    }

    @DisplayName("예약 정보를 저장한다.")
    @Test
    void saveReservationTest() {
        // Given
        ReservationTime savedReservationTime = new ReservationTime(1L, LocalTime.now().plusHours(3));
        Theme savedTheme = new Theme(1L, new ThemeName("테바의 비밀친구"), new ThemeDescription("테바의 은밀한 비밀친구"), "대충 테바 사진 링크");
        Reservation savedReservation = new Reservation(1L, new ClientName("켈리"), LocalDate.now().plusDays(5), savedReservationTime, savedTheme);
        SaveReservationRequest saveReservationRequest = new SaveReservationRequest(LocalDate.now().plusDays(5), "켈리", 1L, 1L);

        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(savedReservationTime));
        given(themeRepository.findById(1L)).willReturn(Optional.of(savedTheme));
        given(reservationRepository.save(saveReservationRequest.toReservation(savedReservationTime, savedTheme))).willReturn(savedReservation);

        // When
        Reservation reservation = reservationService.saveReservation(saveReservationRequest);

        // Then
        assertThat(reservation).isEqualTo(savedReservation);
    }

    @DisplayName("저장하려는 예약 시간이 존재하지 않는다면 예외를 발생시킨다.")
    @Test
    void throwExceptionWhenSaveReservationWithNotExistReservationTimeTest() {
        // Given
        SaveReservationRequest saveReservationRequest = new SaveReservationRequest(LocalDate.now(), "켈리", 1L, 1L);

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
        ReservationTime savedReservationTime = new ReservationTime(1L, LocalTime.now().plusHours(3));
        Theme savedTheme = new Theme(1L, new ThemeName("테바의 비밀친구"), new ThemeDescription("테바의 은밀한 비밀친구"), "대충 테바 사진 링크");
        Reservation savedReservation = new Reservation(1L, new ClientName("켈리"), LocalDate.now().plusDays(5), savedReservationTime, savedTheme);

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

    @DisplayName("전체 예약 시간 정보를 조회한다.")
    @Test
    void getReservationTimesTest() {
        // Given
        List<ReservationTime> savedReservationTimes = List.of(
                new ReservationTime(1L, LocalTime.now().plusHours(3)),
                new ReservationTime(2L, LocalTime.now().plusHours(4))
        );

        given(reservationTimeRepository.findAll()).willReturn(savedReservationTimes);

        // When
        List<ReservationTime> reservationTimes = reservationService.getReservationTimes();

        // Then
        assertThat(reservationTimes).hasSize(savedReservationTimes.size());
    }

    @DisplayName("예약 시간 정보를 저장한다.")
    @Test
    void saveReservationTimeTest() {
        // Given
        ReservationTime savedReservationTime = new ReservationTime(1L, LocalTime.now().plusHours(3));
        SaveReservationTimeRequest saveReservationTimeRequest = new SaveReservationTimeRequest(LocalTime.now().plusHours(3));

        given(reservationTimeRepository.save(saveReservationTimeRequest.toReservationTime())).willReturn(savedReservationTime);

        // When
        ReservationTime reservationTime = reservationService.saveReservationTime(saveReservationTimeRequest);

        // Then
        assertThat(reservationTime).isEqualTo(savedReservationTime);
    }

    @DisplayName("예약 시간 정보를 삭제한다.")
    @Test
    void deleteReservationTimeTest() {
        // Given
        ReservationTime savedReservationTime = new ReservationTime(1L, LocalTime.now().plusHours(3));

        given(reservationTimeRepository.findById(1L)).willReturn(Optional.of(savedReservationTime));
        willDoNothing().given(reservationTimeRepository).deleteById(1L);

        // When & Then
        assertThatCode(() -> reservationService.deleteReservationTime(1L))
                .doesNotThrowAnyException();
    }

    @DisplayName("존재하지 않는 예약 시간 정보를 삭제하려고 하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenDeleteNotExistReservationTimeTest() {
        // Given
        given(reservationTimeRepository.findById(1L)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> reservationService.deleteReservationTime(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 id의 예약 시간이 존재하지 않습니다.");
    }
}
