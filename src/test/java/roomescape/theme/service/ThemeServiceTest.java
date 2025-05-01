package roomescape.theme.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.common.util.DateTime;
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
        ThemeRepository themeRepository = new FakeThemeRepository(null);
        Theme theme = new Theme(null, "테스트", "설명", "썸네일");

        themeRepository.save(theme);
        theme = themeRepository.findById(1L);

        ReservationRepository reservationRepository = new FakeReservationRepository();
        reservationRepository.save(new Reservation(null, "홍길동", LocalDate.of(2025, 12, 5), null, theme));

        DateTime dateTime = new DateTime() {
            @Override
            public LocalDateTime now() {
                return LocalDateTime.of(2025, 5, 1, 10, 0);
            }
        };

        themeService = new ThemeService(dateTime, themeRepository, reservationRepository);
    }

    @DisplayName("존재하는 예약의 테마는 삭제할 수 없다.")
    @Test
    void test() {
        Assertions.assertThatThrownBy(() -> themeService.deleteThemeById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
