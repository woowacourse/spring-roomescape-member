package roomescape.theme.service;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRepository;
import roomescape.reservation.service.FakeReservationRepository;
import roomescape.reservation.service.FakeThemeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeRepository;

class ThemeServiceTest {

    private ThemeService themeService;

    @BeforeEach
    void beforeEach() {
        ThemeRepository themeRepository = new FakeThemeRepository();
        Theme theme = new Theme(null, "테스트", "설명", "썸네일");

        themeRepository.save(theme);
        theme = themeRepository.findById(1L);

        ReservationRepository reservationRepository = new FakeReservationRepository();
        reservationRepository.save(new Reservation(null, "홍길동", LocalDate.of(2025, 12, 5), null, theme));

        themeService = new ThemeService(themeRepository, reservationRepository);
    }

    @DisplayName("존재하는 예약의 테마는 삭제할 수 없다.")
    @Test
    void test() {
        Assertions.assertThatThrownBy(() -> themeService.deleteThemeById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
