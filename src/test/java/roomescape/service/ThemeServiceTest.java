package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(scripts = "/reset_test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class ThemeServiceTest {
    @Autowired
    private ThemeService themeService;

    @Test
    @DisplayName("테마를 사용하는 예약이 존재하면, 삭제하지 않는다.")
    void cantDelete() {
        // given
        Long id = 1L;

        // when, then
        assertThatThrownBy(() -> themeService.deleteTheme(id))
                .isInstanceOf(IllegalStateException.class);
    }
}
