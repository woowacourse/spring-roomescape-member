package roomescape.domain.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.theme.dto.AdminThemeResponse;
import roomescape.domain.theme.dto.CreateThemeRequest;
import roomescape.domain.theme.dto.CreateThemeResponse;
import roomescape.domain.theme.dto.ThemeResponse;
import roomescape.support.fake.FakeReservationRepository;
import roomescape.support.fake.FakeThemeRepository;

class ThemeServiceTest {

    private FakeReservationRepository reservationRepository;
    private FakeThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        themeRepository = new FakeThemeRepository();
    }

    @Test
    void 관리자용_테마_목록을_조회한다() {
        // given
        themeRepository.save(Theme.createWithoutId("미스터리", "보예의 미스터리", "theme-url"));
        ThemeService themeService = new ThemeService(themeRepository, reservationRepository);

        // when
        List<AdminThemeResponse> responses = themeService.getAllThemeForAdmin();

        // then
        assertSoftly(softly -> {
            assertThat(responses).hasSize(1);
            assertThat(responses.getFirst().id()).isEqualTo(1L);
            assertThat(responses.getFirst().name()).isEqualTo("미스터리");
            assertThat(responses.getFirst().content()).isEqualTo("보예의 미스터리");
            assertThat(responses.getFirst().url()).isEqualTo("theme-url");
        });
    }

    @Test
    void 사용자용_테마_목록을_조회한다() {
        // given
        themeRepository.save(Theme.createWithoutId("미스터리", "보예의 미스터리", "theme-url"));
        ThemeService themeService = new ThemeService(themeRepository, reservationRepository);

        // when
        List<ThemeResponse> responses = themeService.getAllTheme();

        // then
        assertSoftly(softly -> {
            assertThat(responses).hasSize(1);
            assertThat(responses.getFirst().id()).isEqualTo(1L);
            assertThat(responses.getFirst().name()).isEqualTo("미스터리");
            assertThat(responses.getFirst().content()).isEqualTo("보예의 미스터리");
            assertThat(responses.getFirst().url()).isEqualTo("theme-url");
        });
    }

    @Test
    void 테마를_생성한다() {
        // given
        ThemeService themeService = new ThemeService(themeRepository, reservationRepository);

        // when
        CreateThemeResponse response = themeService.createTheme(
            new CreateThemeRequest("미스터리", "보예의 미스터리", "theme-url")
        );
        Theme theme = themeRepository.findById(response.id()).orElseThrow();

        // then
        assertSoftly(softly -> {
            assertThat(response.id()).isEqualTo(theme.getId());
            assertThat(response.name()).isEqualTo("미스터리");
            assertThat(response.content()).isEqualTo("보예의 미스터리");
            assertThat(response.url()).isEqualTo("theme-url");
            assertThat(theme.getName()).isEqualTo("미스터리");
            assertThat(theme.getContent()).isEqualTo("보예의 미스터리");
            assertThat(theme.getUrl()).isEqualTo("theme-url");
        });
    }

    @Test
    void 테마를_삭제한다() {
        // given
        Theme theme = themeRepository.save(
            Theme.createWithoutId("공포", "무섭다", "theme-url")
        );
        ThemeService themeService = new ThemeService(themeRepository, reservationRepository);

        // when
        themeService.deleteTheme(theme.getId());

        // then
        assertThat(themeRepository.findById(theme.getId())).isEmpty();
    }
}
