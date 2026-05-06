package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequestDto;
import roomescape.service.fake.FakeThemeDao;

public class ThemeServiceTest {
    private ThemeService themeService;
    private ThemeDao themeDao;

    private ThemeRequestDto requestDto1;
    private ThemeRequestDto requestDto2;

    @BeforeEach
    void setUp() {
        themeDao = new FakeThemeDao();
        themeService = new ThemeService(themeDao);

        requestDto1 = new ThemeRequestDto("테마1", "url", "설명길이제한때문에");
        requestDto2 = new ThemeRequestDto("테마2", "url", "설명길이제한때문에");
    }

    @Test
    void 시간이_이미_존재한다면_예외를_반환한다() {
        insertHandler(requestDto1);
        assertThatThrownBy(() -> themeService.create(requestDto1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Theme insertHandler(ThemeRequestDto requestDto1) {
        return themeService.create(requestDto1);
    }


    @Test
    void 조회하려는_id가_존재하지_않으면_예외_처리한다() {
        Long notExistsThemeId = 1L;

        assertThatThrownBy(() -> themeService.findById(notExistsThemeId))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    void 삭제하려는_id가_존재하지_않으면_예외_처리한다() {
        Long notExistsThemeId = 1L;

        assertThatThrownBy(() -> themeService.delete(notExistsThemeId))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
