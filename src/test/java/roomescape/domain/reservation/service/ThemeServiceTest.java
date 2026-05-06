package roomescape.domain.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.domain.theme.request.ThemeCreateRequest;
import roomescape.domain.theme.response.ThemeResponse;
import roomescape.domain.theme.service.ThemeService;

@ExtendWith(MockitoExtension.class)
public class ThemeServiceTest {

    @Mock
    ThemeRepository themeRepository;

    @InjectMocks
    ThemeService themeService;

    @Test
    @DisplayName("테마를 성공적으로 생성한다.")
    void saveTheme() {
        //given
        Theme theme = new Theme(1L, "테마", "요약", "www.url.com");
        when(themeRepository.save(any(Theme.class)))
                .thenReturn(theme);

        ThemeCreateRequest request = new ThemeCreateRequest("테마", "요약", "www.url.com");

        //when
        ThemeResponse response = themeService.saveTheme(request);

        //then
        assertThat(response.id()).isEqualTo(theme.getId());
        assertThat(response.name()).isEqualTo("테마");
        assertThat(response.description()).isEqualTo("요약");
        assertThat(response.thumbnailUrl()).isEqualTo("www.url.com");

        verify(themeRepository).save(any(Theme.class));
    }

    @Test
    @DisplayName("모든 테마를 조회한다.")
    void findAllThemes() {
        //given
        List<Theme> themes = new ArrayList<>();
        themes.add(new Theme(1L, "테마A", "요약", "www.url.com/A"));
        themes.add(new Theme(2L, "테마B", "요약", "www.url.com/B"));

        when(themeRepository.findAll()).thenReturn(themes);

        // when
        List<ThemeResponse> responses = themeService.findAllThemes().themes();

        // then
        assertThat(responses).hasSize(2)
                .extracting("id", "name","description", "thumbnailUrl")
                .containsExactly(
                        tuple(1L, "테마A", "요약", "www.url.com/A"),
                        tuple(2L, "테마B", "요약", "www.url.com/B")
                );

        verify(themeRepository).findAll();
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteReservationBy() {
        // given
        Long themeId = 1L;

        // when
        themeService.deleteThemeById(themeId);

        // then
        verify(themeRepository).deleteById(themeId);
    }
}
