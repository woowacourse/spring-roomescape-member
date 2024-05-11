package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;
import roomescape.application.dto.request.ThemeCreationRequest;
import roomescape.application.dto.response.ThemeResponse;
import roomescape.support.annotation.FixedClock;
import roomescape.support.annotation.WithoutWebSpringBootTest;

@WithoutWebSpringBootTest
@FixedClock(date = "2024-05-03")
@Sql("/theme.sql")
class ThemeServiceTest {
    @Autowired
    private ThemeService themeService;

    @Test
    void 테마_저장을_성공한다() {
        ThemeCreationRequest request = new ThemeCreationRequest("포레스트", "설명", "https://forest.com");

        ThemeResponse response = themeService.save(request);

        assertAll(
                () -> assertThat(response.name()).isEqualTo(request.name()),
                () -> assertThat(response.description()).isEqualTo(request.description()),
                () -> assertThat(response.thumbnail()).isEqualTo(request.thumbnail())
        );
    }

    @Test
    void 중복_이름의_테마를_저장할_경우_예외가_발생한다() {
        ThemeCreationRequest request = new ThemeCreationRequest("셜록홈즈", "설명", "https://sherlock.com");

        assertThatThrownBy(() -> themeService.save(request))
                .isExactlyInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void 테마를_삭제한다() {
        themeService.delete(2L);

        assertThat(themeService.findThemes()).hasSize(2);
    }

    @Test
    void 존재하지_않는_테마를_삭제하면_예외가_발생한다() {
        assertThatThrownBy(() -> themeService.delete(0L))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 테마입니다.");
    }

    @Test
    @Sql("/reservation.sql")
    void 예약에서_사용_중인_테마를_삭제하면_예외가_발생한다() {
        assertThatThrownBy(() -> themeService.delete(1L))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 테마를 사용하는 예약이 존재합니다.");
    }

    @Test
    @Sql("/reservation.sql")
    void 인기_테마를_조회한다() {
        List<ThemeResponse> popularThemes = themeService.findPopularThemes();

        assertAll(
                () -> assertThat(popularThemes).hasSize(3),
                () -> assertThat(popularThemes.get(0).name()).isEqualTo("셜록홈즈"),
                () -> assertThat(popularThemes.get(1).name()).isEqualTo("이순신"),
                () -> assertThat(popularThemes.get(2).name()).isEqualTo("해리포터")
        );
    }
}

