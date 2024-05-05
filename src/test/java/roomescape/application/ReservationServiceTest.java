package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.application.dto.ReservationRequest;
import roomescape.application.dto.ReservationResponse;
import roomescape.domain.PlayerName;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationQueryRepository;
import roomescape.domain.ReservationCommandRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;
import roomescape.domain.ThemeRepository;

@ServiceTest
class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationCommandRepository reservationCommandRepository;

    @Autowired
    private ReservationQueryRepository reservationQueryRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("정상적인 예약 요청을 받아서 저장한다.")
    @Test
    void shouldReturnReservationResponseWhenValidReservationRequestSave() {
        ReservationTime time = reservationTimeRepository.create(new ReservationTime(LocalTime.of(12, 0)));
        Theme theme = themeRepository.create(new Theme(new ThemeName("themeName"), "desc", "url"));
        ReservationRequest reservationRequest = new ReservationRequest(
                "test",
                LocalDate.of(2024, 1, 1),
                time.getId(),
                theme.getId()
        );

        reservationService.create(reservationRequest);

        List<Reservation> reservations = reservationQueryRepository.findAll();
        assertThat(reservations).hasSize(1);
    }

    @DisplayName("존재하지 않는 예약 시간으로 예약을 생성시 IllegalArgumentException 예외를 반환한다.")
    @Test
    void shouldReturnIllegalArgumentExceptionWhenNotFoundReservationTime() {
        Theme savedTheme = themeRepository.create(new Theme(new ThemeName("test"), "test", "test"));
        ReservationRequest request = new ReservationRequest("test", LocalDate.now(), 99L, savedTheme.getId());

        assertThatCode(() -> reservationService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 시간 입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 테마로 예약을 생성시 IllegalArgumentException 예외를 반환한다.")
    void shouldThrowIllegalArgumentExceptionWhenNotFoundTheme() {
        ReservationTime time = reservationTimeRepository.create(new ReservationTime(LocalTime.of(12, 0)));
        ReservationRequest request = new ReservationRequest(
                "test",
                LocalDate.of(2024, 1, 1),
                time.getId(),
                99L
        );
        assertThatCode(() -> reservationService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테마 입니다.");
    }


    @DisplayName("중복된 예약을 하는 경우 IllegalStateException 예외를 반환한다.")
    @Test
    void shouldReturnIllegalStateExceptionWhenDuplicatedReservationCreate() {
        ReservationTime time = reservationTimeRepository.create(new ReservationTime(LocalTime.of(10, 0)));
        Theme theme = themeRepository.create(new Theme(new ThemeName("test"), "test", "test"));
        ReservationRequest request = new ReservationRequest(
                "test",
                LocalDate.of(2024, 12, 25),
                time.getId(),
                theme.getId()
        );
        reservationCommandRepository.create(request.toReservation(time, theme));

        assertThatCode(() -> reservationService.create(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 예약입니다.");
    }

    @DisplayName("과거 시간을 예약하는 경우 IllegalArgumentException 예외를 반환한다.")
    @Test
    void shouldThrowsIllegalArgumentExceptionWhenReservationDateIsBeforeCurrentDate() {
        ReservationTime time = reservationTimeRepository.create(new ReservationTime(1L, LocalTime.of(12, 0)));
        Theme theme = themeRepository.create(new Theme(new ThemeName("test"), "test", "test"));
        ReservationRequest reservationRequest = new ReservationRequest("test", LocalDate.of(1999, 12, 25), time.getId(),
                theme.getId());

        assertThatCode(() -> reservationService.create(reservationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("현재 시간보다 과거로 예약할 수 없습니다.");
    }

    @DisplayName("모든 예약을 조회한다.")
    @Test
    void shouldReturnReservationResponsesWhenReservationsExist() {
        saveReservation();
        List<ReservationResponse> reservationResponses = reservationService.findAll();
        assertThat(reservationResponses).hasSize(1);
    }

    @DisplayName("예약 삭제 요청시 예약이 존재하면 예약을 삭제한다.")
    @Test
    void shouldDeleteReservationWhenReservationExist() {
        Reservation reservation = saveReservation();
        reservationService.deleteById(reservation.getId());

        List<Reservation> reservations = reservationQueryRepository.findAll();
        assertThat(reservations).isEmpty();
    }

    @DisplayName("예약 삭제 요청시 예약이 존재하지 않으면 IllegalArgumentException 예외를 반환한다.")
    @Test
    void shouldThrowsIllegalArgumentExceptionWhenReservationDoesNotExist() {
        assertThatCode(() -> reservationService.deleteById(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 예약 입니다.");
    }

    private Reservation saveReservation() {
        ReservationTime time = reservationTimeRepository.create(new ReservationTime(LocalTime.of(10, 0)));
        Theme theme = themeRepository.create(new Theme(new ThemeName("test"), "test", "test"));
        Reservation reservation = new Reservation(new PlayerName("test"), LocalDate.of(2024, 1, 1), time, theme);
        return reservationCommandRepository.create(reservation);
    }
}
