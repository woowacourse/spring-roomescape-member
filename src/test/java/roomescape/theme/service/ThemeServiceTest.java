package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.FakeReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeInUseException;
import roomescape.reservationtime.repository.FakeReservationTimeRepository;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.dto.request.ThemeCreateRequest;
import roomescape.theme.dto.response.ThemeResponse;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.FakeThemeRepository;
import roomescape.theme.repository.ThemeRepository;

class ThemeServiceTest {
    private ThemeService themeService;
    private ReservationRepository reservationRepository;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository();
        themeRepository = new FakeThemeRepository(reservationRepository);
        themeService = new ThemeService(themeRepository, reservationRepository);
    }

    @Test
    void create_shouldReturnThemeResponse_whenSuccessful() {
        ThemeCreateRequest request = new ThemeCreateRequest("추리", "셜록 추리 게임", "image.png");

        ThemeResponse response = themeService.create(request);

        assertThat(response.name()).isEqualTo("추리");
        assertThat(response.description()).isEqualTo("셜록 추리 게임");
    }

    @Test
    void getThemes_shouldReturnAllThemes() {
        themeService.create(new ThemeCreateRequest("추리", "셜록 추리 게임", "image.png"));
        themeService.create(new ThemeCreateRequest("모험", "모험의 세계", "image2.png"));

        List<ThemeResponse> themes = themeService.getThemes();

        assertThat(themes).hasSize(2);
    }

    @Test
    void delete_shouldThrowException_whenThemeNotFound() {
        assertThatThrownBy(() -> themeService.delete(999L))
                .isInstanceOf(ThemeNotFoundException.class)
                .hasMessageContaining("요청한 id와 일치하는 테마 정보가 없습니다.");
    }

    @Test
    void delete_shouldThrowException_whenReservationExists() {
        Theme theme = themeRepository.put(Theme.of(1L, "추리", "셜록 추리 게임", "image.png"));
        ReservationTime time = reservationTimeRepository.put(
                ReservationTime.withUnassignedId(LocalTime.parse("10:00")));
        reservationRepository.put(
                Reservation.withUnassignedId(LocalDate.now().plusDays(1), "danny", time,
                        theme));

        assertThatThrownBy(() -> themeService.delete(theme.getId()))
                .isInstanceOf(ReservationTimeInUseException.class)
                .hasMessage("해당 테마에 대한 예약이 존재하여 삭제할 수 없습니다.");
    }

    @Test
    void delete_shouldRemoveTheme_whenNoReservationExists() {
        Theme theme = themeRepository.put(Theme.of(1L, "추리", "셜록 추리 게임", "image.png"));

        themeService.delete(theme.getId());

        List<ThemeResponse> themes = themeService.getThemes();
        assertThat(themes).isEmpty();
    }
}
