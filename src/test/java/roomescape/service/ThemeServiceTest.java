package roomescape.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.entity.Theme;
import roomescape.exception.impl.ThemeNotFoundException;
import roomescape.repository.JdbcThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private JdbcThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        themeRepository.save(Theme.beforeSave(
                "스테이지",
                "무엇보다 무서운",
                "공포테마")
        );
    }


    @Test
    @DisplayName("없는 테마 삭제시 예외가 발생한다.")
    void whenDeleteNonExistThemeThrowExceptionTest() {
        // given
        // when
        // then
        Assertions.assertThatThrownBy(() -> themeService.deleteById(2))
                .isInstanceOf(ThemeNotFoundException.class);
    }
}
