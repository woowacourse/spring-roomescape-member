package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.TestFixtures;
import roomescape.controller.response.ThemeResponse;

class ThemeServiceTest {
    private final FakeThemeRepository fakeThemeRepository = new FakeThemeRepository();
    private final FakeReservationRepository fakeReservationRepository = new FakeReservationRepository();
    private final ThemeService themeService = new ThemeService(
            fakeThemeRepository, fakeReservationRepository
    );

    @BeforeEach
    void setUp() {
        fakeReservationRepository.deleteAll();
        fakeThemeRepository.deleteAll();
        fakeThemeRepository.save(TestFixtures.THEME_1);
        fakeThemeRepository.save(TestFixtures.THEME_2);
        fakeReservationRepository.save(TestFixtures.PAST_RESERVATION_1);
    }

    @DisplayName("전체 테마를 반환한다")
    @Test
    void findAll() {
        List<ThemeResponse> themeResponses = themeService.findAll();

        assertThat(themeResponses).isEqualTo(List.of(TestFixtures.THEME_RESPONSE_1, TestFixtures.THEME_RESPONSE_2));
    }

    @DisplayName("테마를 저장한다")
    @Test
    void save() {
        themeService.save(TestFixtures.THEME_REQUEST_3);

        assertThat(themeService.findAll()).isEqualTo(List.of(TestFixtures.THEME_RESPONSE_1, TestFixtures.THEME_RESPONSE_2, TestFixtures.THEME_RESPONSE_3));
    }

    @DisplayName("해당 id의 테마를 삭제한다")
    @Test
    void deleteById() {
        themeService.deleteById(1L);

        assertThat(themeService.findAll()).isEqualTo(List.of(TestFixtures.THEME_RESPONSE_2));
    }

    @DisplayName("인기 많은 테마를 반환한다")
    @Test
    void findTopThemes() {
        List<ThemeResponse> topThemes1 = themeService.findTopThemes();
        fakeReservationRepository.save(TestFixtures.PAST_RESERVATION_2);
        fakeReservationRepository.save(TestFixtures.PAST_RESERVATION_3);
        List<ThemeResponse> topThemes2 = themeService.findTopThemes();

        Assertions.assertAll(
                () -> assertThat(topThemes1).isEqualTo(List.of(TestFixtures.THEME_RESPONSE_1)),
                () -> assertThat(topThemes2).isEqualTo(List.of(TestFixtures.THEME_RESPONSE_2, TestFixtures.THEME_RESPONSE_1))
        );
    }
}
