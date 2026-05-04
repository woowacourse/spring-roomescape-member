package roomescape.theme.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Test
    void 새로운_테마를_생성하고_쩡상적으로_응답을_반환한다() {
        ThemeRequest request = new ThemeRequest("공포 테마", "무서워", "무서워");

        ThemeResponse response = themeService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getName()).isEqualTo("공포 테마");
        assertThat(response.getDescription()).isEqualTo("무서워");
        assertThat(response.getImageUrl()).isEqualTo("무서워");
    }

    @Test
    void 테마를_정상적으로_삭제한다() {
        ThemeRequest request = new ThemeRequest("코믹 테마", "웃겨", "웃겨");
        ThemeResponse response = themeService.create(request);

        assertDoesNotThrow(() -> themeService.delete(response.getId()));
    }
}
