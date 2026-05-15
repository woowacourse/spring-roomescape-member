package roomescape.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.RoomescapeException;

@SpringBootTest
@Transactional
public class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Test
    void 존재하지_않는_테마를_삭제하는경우_예외가_발생한다() {
        Assertions.assertThatThrownBy(() -> themeService.removeById(-1L))
                .isInstanceOf(RoomescapeException.class);
    }

    @Test
    void 존재하는_테마를_삭제하는경우_삭제된다() {
        Assertions.assertThatCode(() -> themeService.removeById(7L))
                .doesNotThrowAnyException();
    }
}
