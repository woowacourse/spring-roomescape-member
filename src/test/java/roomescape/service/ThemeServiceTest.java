package roomescape.service;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Theme;
import roomescape.dto.ThemeCreateRequest;
import roomescape.dto.ThemeResponse;
import roomescape.repository.ThemeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@DisplayName("테마 서비스")
class ThemeServiceTest {

    private ThemeService themeService;
    @Mock
    private ThemeRepository themeRepository;
    private final Theme themeFixture = new Theme(
            "공포",
            "완전 무서운 테마",
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    );

    @BeforeEach
    void setUp() {
        this.themeService = new ThemeService(themeRepository);
    }

    @DisplayName("테마 서비스는 테마를 생성한다.")
    @Test
    void createTheme() {
        // given
        Mockito.when(themeRepository.save(any()))
                .thenReturn(themeFixture);
        ThemeCreateRequest request = new ThemeCreateRequest(
                "공포",
                "완전 무서운 테마",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        // when
        ThemeResponse actual = themeService.createTheme(request);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(actual.name()).isEqualTo(themeFixture.getName());
        softAssertions.assertThat(actual.description()).isEqualTo(themeFixture.getDescription());
        softAssertions.assertThat(actual.thumbnail()).isEqualTo(themeFixture.getThumbnail());
        softAssertions.assertAll();
    }

    @DisplayName("테마 서비스는 모든 테마를 조회한다.")
    @Test
    void findAll() {
        // given
        Mockito.when(themeRepository.findAll())
                .thenReturn(List.of(themeFixture));

        // when
        List<ThemeResponse> themeResponses = themeService.readThemes();

        // then
        assertThat(themeResponses).hasSize(1);
    }

    @DisplayName("테마 서비스는 id에 해당하는 테마를 삭제한다.")
    @Test
    void delete() {
        // given
        Long id = 1L;
        Mockito.doNothing().when(themeRepository).delete(id);

        // when & then
        assertThatCode(() -> themeService.deleteTheme(id))
                .doesNotThrowAnyException();
    }
}
