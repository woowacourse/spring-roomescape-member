package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;
import roomescape.application.dto.ThemeCreationRequest;
import roomescape.domain.theme.Theme;
import roomescape.support.annotation.FixedClock;
import roomescape.support.extension.MockClockExtension;
import roomescape.support.extension.TableTruncateExtension;

@SpringBootTest
@ExtendWith({TableTruncateExtension.class, MockClockExtension.class})
@FixedClock(date = "2024-05-03")
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
                .isExactlyInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void 테마를_삭제한다() {
        ThemeCreationRequest request = new ThemeCreationRequest("테마1", "설명", "썸네일1");
        Theme theme = themeService.save(request);

        themeService.delete(theme.getId());

        assertThat(themeService.findThemes()).isEmpty();
    }

    @Test
    void 존재하지_않는_테마를_삭제하면_예외가_발생한다() {
        assertThatThrownBy(() -> themeService.delete(0L))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 테마입니다.");
    }

    @Test
        // todo sql
    void 예약에서_사용_중인_테마를_삭제하면_예외가_발생한다() {
        ThemeCreationRequest request = new ThemeCreationRequest("테마1", "설명", "썸네일1");
        Theme theme = themeService.save(request);

        assertThatThrownBy(() -> themeService.delete(theme.getId()))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 테마를 사용하는 예약이 존재합니다.");
    }

    @Test
    @Sql("/reservation.sql")
    void 인기_테마를_조회한다() {
        List<Theme> popularThemes = themeService.findPopularThemes();

        assertAll(
                () -> assertThat(popularThemes).hasSize(3),
                () -> assertThat(popularThemes.get(0).getName()).isEqualTo("테마1"),
                () -> assertThat(popularThemes.get(1).getName()).isEqualTo("테마3"),
                () -> assertThat(popularThemes.get(2).getName()).isEqualTo("테마2")
        );
    }
}
