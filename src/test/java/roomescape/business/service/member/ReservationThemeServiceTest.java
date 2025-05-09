package roomescape.business.service.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.persistence.ReservationThemeRepository;
import roomescape.persistence.fakerepository.FakeReservationThemeRepository;

class ReservationThemeServiceTest {

    private ReservationThemeService reservationThemeService;
    private ReservationThemeRepository reservationThemeRepository;

    @BeforeEach
    void setUp() {
        reservationThemeRepository = new FakeReservationThemeRepository();
        reservationThemeService = new ReservationThemeService(reservationThemeRepository);
    }

    @DisplayName("테마 목록을 조회한다.")
    @Test
    void getAllThemes() {
        // given
        reservationThemeRepository.add(new ReservationTheme("테마1", "설명1", "썸네일1"));
        reservationThemeRepository.add(new ReservationTheme("테마2", "설명2", "썸네일2"));

        // when
        var themes = reservationThemeService.getAllThemes();

        // then
        assertThat(themes)
                .hasSize(2);
    }
}
