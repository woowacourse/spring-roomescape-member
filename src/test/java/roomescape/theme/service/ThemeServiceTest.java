package roomescape.theme.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
import roomescape.theme.dto.PopularThemeResponse;

class ThemeServiceTest {

    private ThemeService themeService;

    @BeforeEach
    void beforeEach() {
        Theme theme1 = Theme.createWithId(1L, "테스트1", "설명", "썸네일");
        Theme theme2 = Theme.createWithId(2L, "테스트2", "설명", "썸네일");
        Theme theme3 = Theme.createWithId(3L, "테스트3", "설명", "썸네일");

        ReservationRepository reservationRepository = new FakeReservationRepository();
        reservationRepository.save(Reservation.createWithoutId("홍길동1", LocalDate.of(2025, 12, 5), null, theme1));
        reservationRepository.save(Reservation.createWithoutId("홍길동2", LocalDate.of(2025, 12, 6), null, theme1));
        reservationRepository.save(Reservation.createWithoutId("홍길동3", LocalDate.of(2025, 12, 4), null, theme3));

        ThemeRepository themeRepository = new FakeThemeRepository(reservationRepository);
        themeRepository.save(theme1);
        themeRepository.save(theme2);
        themeRepository.save(theme3);

        DateTime dateTime = new DateTime() {
            @Override
            public LocalDateTime now() {
                return LocalDateTime.of(2025, 12, 7, 10, 0);
            }
        };

        themeService = new ThemeService(dateTime, themeRepository, reservationRepository);
    }

    @DisplayName("존재하는 예약의 테마는 삭제할 수 없다.")
    @Test
    void can_not_remove_exists_reservation() {
        Assertions.assertThatThrownBy(() -> themeService.deleteThemeById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인기 테마를 가져올 수 있다.")
    @Test
    void can_get_popular_theme() {
        List<PopularThemeResponse> popularThemes = themeService.getPopularThemes();

        Assertions.assertThat(popularThemes).containsExactly(
                new PopularThemeResponse("테스트1", "썸네일", "설명"),
                new PopularThemeResponse("테스트3", "썸네일", "설명")
        );
    }
}
