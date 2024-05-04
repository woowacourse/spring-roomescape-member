package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;
import roomescape.service.dto.request.ReservationRequest;
import roomescape.service.dto.response.ReservationResponse;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(Instant.parse("2024-04-09T00:00:00Z"), ZoneId.systemDefault());

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private Clock clock;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("모든 예약들을 조회한다.")
    void getAllReservations() {
        Reservation reservation = getReservation();
        BDDMockito.given(reservationRepository.findAll())
                .willReturn(List.of(reservation));

        List<ReservationResponse> reservationResponses = reservationService.getAllReservations();

        ReservationResponse expected = ReservationResponse.from(reservation);
        assertThat(reservationResponses).containsExactly(expected);
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void addReservation() {
        Reservation reservation = getReservation();
        BDDMockito.given(reservationTimeRepository.getById(anyLong()))
                .willReturn(getReservationTime());
        BDDMockito.given(themeRepository.getById(anyLong()))
                .willReturn(getTheme());
        BDDMockito.given(reservationRepository.save(any()))
                .willReturn(reservation);
        BDDMockito.given(reservationRepository.existsByReservation(any(), anyLong(), anyLong()))
                .willReturn(false);
        BDDMockito.given(clock.instant())
                .willReturn(Instant.now(FIXED_CLOCK));
        BDDMockito.given(clock.getZone())
                .willReturn(FIXED_CLOCK.getZone());

        ReservationRequest reservationRequest = new ReservationRequest("예약", "2024-04-09", 1L, 1L);
        ReservationResponse reservationResponse = reservationService.addReservation(reservationRequest);

        ReservationResponse expected = ReservationResponse.from(reservation);
        assertThat(reservationResponse).isEqualTo(expected);
    }

    @Test
    @DisplayName("이미 존재하는 예약을 추가할 수 없다.")
    void addExistingReservation() {
        BDDMockito.given(reservationTimeRepository.getById(anyLong()))
                .willReturn(getReservationTime());
        BDDMockito.given(themeRepository.getById(anyLong()))
                .willReturn(getTheme());
        BDDMockito.given(reservationRepository.existsByReservation(any(), anyLong(), anyLong()))
                .willReturn(true);

        ReservationRequest reservationRequest = new ReservationRequest("예약", "2024-04-09", 1L, 1L);

        assertThatThrownBy(() -> reservationService.addReservation(reservationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 날짜/시간에 이미 예약이 존재합니다.");
    }

    @Test
    @DisplayName("지난 날짜/시간에 대한 예약은 불가능하다.")
    void addReservationWithPassedDateTime() {
        BDDMockito.given(reservationTimeRepository.getById(anyLong()))
                .willReturn(getReservationTime());
        BDDMockito.given(themeRepository.getById(anyLong()))
                .willReturn(getTheme());
        BDDMockito.given(clock.instant())
                .willReturn(Instant.now(FIXED_CLOCK));
        BDDMockito.given(clock.getZone())
                .willReturn(FIXED_CLOCK.getZone());

        ReservationRequest reservationRequest = new ReservationRequest("예약", "2024-04-08", 1L, 1L);

        assertThatThrownBy(() -> reservationService.addReservation(reservationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지나간 날짜/시간에 대한 예약은 불가능합니다.");
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제할 수 없다.")
    void deleteNotExistedReservation() {
        BDDMockito.given(reservationRepository.existsById(anyLong()))
                .willReturn(false);

        assertThatThrownBy(() -> reservationService.deleteReservationById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 id의 예약이 존재하지 않습니다.");
    }

    private Reservation getReservation() {
        ReservationTime reservationTime = getReservationTime();
        Theme theme = getTheme();
        return new Reservation(1L, "예약", LocalDate.of(2024, 4, 9), reservationTime, theme);
    }

    private ReservationTime getReservationTime() {
        return new ReservationTime(1L, LocalTime.of(10, 0));
    }

    private Theme getTheme() {
        return new Theme(1L, "테마", "테마 설명", "https://example.com");
    }
}
