package roomescape.domain.theme.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.theme.dto.request.ThemeCreatedRequestDTO;
import roomescape.domain.theme.dto.response.ThemeResponseDTO;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.FakeThemeRepository;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.time.dto.response.TimeResponseDTO;
import roomescape.domain.time.entity.Time;

class ThemeServiceTest {

    private final ThemeService themeService;
    private final ThemeRepository themeRepository;

    ThemeServiceTest() {
        this.themeRepository = new FakeThemeRepository();
        this.themeService = new ThemeService(themeRepository);
    }

    @Nested
    class GetThemesTest {

        @Test
        void 성공() {

            // when
            List<ThemeResponseDTO> actual = themeService.getThemes();

            // then
            assertEquals(0, actual.size());
        }
    }

    @Nested
    class saveThemeTest {

        @Test
        void 성공() {

            // given
            ThemeCreatedRequestDTO request = new ThemeCreatedRequestDTO(
                "피온",
                "테마 설명",
                "https://roomescape.com/images/themes/prison-room.png"
            );

            // when
            ThemeResponseDTO actual = themeService.saveTheme(request);

            // then
            assertAll(
                () -> assertEquals(1L, actual.id()),
                () -> assertEquals("피온", actual.name()),
                () -> assertEquals("테마 설명", actual.description()),
                () -> assertEquals("https://roomescape.com/images/themes/prison-room.png", actual.imageUrl())
            );
        }
    }

    @Nested
    class deleteThemeTest {

        @Test
        void 성공() {

            // given
            themeRepository.save(Theme.create("브라운", "테마 설명", "https://roomescape.com/images/themes/prison-room.png"));

            // when

            // then
            List<Theme> actual = themeRepository.findAllThemes();

            assertThat(actual).asInstanceOf(InstanceOfAssertFactories.LIST)
                    .hasSize(0);
        }
    }
}