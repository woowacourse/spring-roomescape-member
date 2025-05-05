package roomescape.reservation.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.repository.FakeReservationRepository;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.FakeThemeRepository;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.repository.FakeTimeRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.reservation.service.dto.request.ReservationRequest;
import roomescape.reservation.entity.Reservation;
import roomescape.time.entity.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationServiceTest {
    private final ReservationRepository reservationRepository = new FakeReservationRepository();
    private final ReservationTimeRepository timeRepository = new FakeTimeRepository();
    private final ThemeRepository themeRepository = new FakeThemeRepository();
    private final ReservationService service = new ReservationService(
            reservationRepository,
            timeRepository,
            themeRepository
    );

    @BeforeEach
    void setupTheme() {
        themeRepository.save(new Theme(1L, "theme", "hello", "hi"));
    }

    @DisplayName("존재하지 않는 timeId로 생성할 수 없다.")
    @Test
    void notExistTimeId() {
        // given
        LocalDate now = LocalDate.now();
        ReservationRequest requestDto = new ReservationRequest(now.plusDays(1), "test", 1L, 1L);

        // when & then
        assertThatThrownBy(() -> {
            service.createReservation(requestDto);
        }).isInstanceOf(IllegalArgumentException.class);
     }

    @DisplayName("과거 날짜/시간 예약은 생성할 수 없다.")
    @Test
    void pastReservation() {
        // given
        LocalDate now = LocalDate.now();
        ReservationRequest requestDto = new ReservationRequest(now.minusDays(1), "test", 1L, 1L);

        // when & then
        assertThatThrownBy(() -> {
            service.createReservation(requestDto);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("같은 날짜, 같은 시각에 이미 예약이 존재하는 경우, 재생성할 수 없다.")
    @Test
    void duplicateReservation() {
        // given
        LocalDate now = LocalDate.now();
        LocalDate date = now.plusDays(1);

        ReservationTime timeEntity = ReservationTime.of(1L, LocalTime.of(12, 0));
        Reservation reservation = Reservation.of(1L, "test", date, timeEntity, 1L);
        timeRepository.save(timeEntity);
        reservationRepository.save(reservation);

        ReservationRequest requestDto = new ReservationRequest(date, "test", timeEntity.getId(), 1L);

        // when & then
        assertThatThrownBy(() -> {
            service.createReservation(requestDto);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
