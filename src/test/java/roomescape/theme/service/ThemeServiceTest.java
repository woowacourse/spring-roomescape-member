package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.FakeThemeRepository;

class ThemeServiceTest {
    private ThemeService themeService;
    private FakeThemeRepository themeRepository;

    @BeforeEach
    void setup(){
        this.themeRepository = new FakeThemeRepository();
        this.themeService = new ThemeService(themeRepository);
    }

    @Test
    @DisplayName("등록된 테마가 여러개이면 조회 시 등록된 갯수만큼 반환한다.")
    void readThemes(){
        // given
        List<Theme> themes = List.of(
            Theme.create( "테마1", "테마1 설명", "테마1 썸네일"),
            Theme.create("테마2", "테마2 설명", "테마2 썸네일"),
            Theme.create("테마3", "테마3 설명", "테마3 썸네일")
        );
        themeRepository.saveAll(themes);

        // when
        List<Theme> actual = themeService.readThemes();

        // then
        assertThat(actual).hasSize(themes.size());
    }

    @Test
    @DisplayName("등록된 테마와 조회되는 테마의 모든 필드가 일치한다.")
    void readTheme(){
        // given
        Theme savedTheme = themeRepository.save( Theme.create("테마1", "테마1 설명", "테마1 썸네일"));

        // when
        Theme actual = themeService.readTheme(savedTheme.id());

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(savedTheme);
    }

    @Test
    @DisplayName("등록되지 않은 테마 조회 시 예외가 발생한다.")
    void readTheme_unregistered(){
        // given
        Long unregisteredId = Long.MIN_VALUE;

        // when & then
        assertThatThrownBy(() -> themeService.readTheme(unregisteredId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테마가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("테마를 1개 등록하면 테마 데이터 수가 1 증가한다.")
    void register(){
        // given
        String name = "테마1";
        String description = "테마1 설명";
        String thumbnail = "테마1 썸네일";

        // when
        themeService.register(name, description, thumbnail);

        // then
        assertThat(themeService.readThemes())
                .hasSize(1);
    }

    @Test
    @DisplayName("등록한 테마와 다시 조회한 테마의 모든 필드가 일치한다.")
    void register_theme_fields_match(){
        // given
        String name = "테마1";
        String description = "테마1 설명";
        String thumbnail = "테마1 썸네일";

        // when
        Theme registeredTheme =  themeService.register(name, description, thumbnail);

        // then
        assertThat(registeredTheme)
                .usingRecursiveComparison()
                .isEqualTo(themeService.readTheme(registeredTheme.id()));
    }


    @Test
    @DisplayName("테마를 활성화한다.")
    void updateStatus_active(){
        // given
        Theme savedTheme = themeRepository.save(Theme.create("테마1", "테마1 설명", "테마1 썸네일"));

        // when
        themeService.updateStatus(savedTheme.id(), true);

        // then
        assertThat(themeRepository.findById(savedTheme.id()).get().isActive())
                .isTrue();
    }


    @Test
    @DisplayName("테마를 비활성화한다.")
    void updateStatus_deactivate(){
        // given
        Theme theme = Theme.create("테마1", "테마1 설명", "테마1 썸네일");
        theme.updateStatus(true);
        Theme savedTheme = themeRepository.save(theme);

        // when
        themeService.updateStatus(savedTheme.id(), false);

        // then
        assertThat(themeRepository.findById(savedTheme.id()).get().isActive())
                .isFalse();
    }

}
