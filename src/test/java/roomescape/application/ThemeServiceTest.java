package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.application.dto.ThemeCreationRequest;
import roomescape.domain.theme.Theme;
import roomescape.support.extension.TableTruncateExtension;

@SpringBootTest
@ExtendWith(TableTruncateExtension.class)
class ThemeServiceTest {
    @Autowired
    private ThemeService themeService;

    @Test
    void 테마_저장을_성공한다() {
        ThemeCreationRequest request = new ThemeCreationRequest("테마1", "설명", "썸네일1");

        Theme theme = themeService.save(request);

        assertAll(
                () -> assertThat(theme.getName()).isEqualTo(request.name()),
                () -> assertThat(theme.getDescription()).isEqualTo(request.description()),
                () -> assertThat(theme.getThumbnail()).isEqualTo(request.thumbnail())
        );
    }

    @Test
    void 중복_이름의_테마를_저장할_경우_예외가_발생한다() {
        ThemeCreationRequest request = new ThemeCreationRequest("테마1", "설명", "썸네일1");
        themeService.save(request);

        assertThatThrownBy(() -> themeService.save(request))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("테마 이름이 존재합니다.");
    }

    @Test
    void 테마를_삭제한다() {
        ThemeCreationRequest request = new ThemeCreationRequest("테마1", "설명", "썸네일1");
        Theme theme = themeService.save(request);

        themeService.delete(theme.getId());

        assertThat(themeService.findThemes()).isEmpty();
    }

    @Test
    void 아이디가_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> themeService.delete(0L))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 테마입니다.");
    }
}
