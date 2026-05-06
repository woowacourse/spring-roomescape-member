package roomescape.theme.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.dto.ThemesResponse;
import roomescape.theme.model.Theme;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@Transactional
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Test
    void 새로운_테마를_생성하고_쩡상적으로_응답을_반환한다() {
        ThemeRequest request = new ThemeRequest("공포 테마", "무서워", "무서워", LocalTime.of(2, 0));

        ThemeResponse response = themeService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("공포 테마");
        assertThat(response.getDescription()).isEqualTo("무서워");
        assertThat(response.getImageUrl()).isEqualTo("무서워");
    }

    @Test
    void 테마를_정상적으로_삭제한다() {
        ThemeRequest request = new ThemeRequest("코믹 테마", "웃겨", "웃겨", LocalTime.of(2, 0));
        ThemeResponse response = themeService.create(request);

        assertDoesNotThrow(() -> themeService.delete(response.getId()));
    }

    @Test
    void 테마를_전체_조회한다() {
        Theme theme1 = new Theme(1L, "테마1", "설명1", "경로1", LocalTime.of(2, 0));
        Theme theme2 = new Theme(2L, "테마2", "설명2", "경로2", LocalTime.of(2, 0));

        ThemeRequest theme1Request = new ThemeRequest(theme1.getName(), theme1.getDescription(), theme1.getImageUrl(), theme1.getRequiredTime());
        ThemeRequest theme2Request = new ThemeRequest(theme2.getName(), theme2.getDescription(), theme2.getImageUrl(), theme2.getRequiredTime());

        themeService.create(theme1Request);
        themeService.create(theme2Request);

        ThemesResponse response = themeService.findAll();

        assertThat(response).isNotNull();
        assertThat(response.getThemeResponses()).hasSize(2);
    }
}
