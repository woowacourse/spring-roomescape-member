package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.exception.ErrorMessage;
import roomescape.exception.custom.ConflictException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ThemeCommandServiceTest {

    @Autowired
    private ThemeCommandService themeCommandService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation_history");
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
    }

    @Test
    @DisplayName("이미 존재하는 테마명으로 등록 시 예외가 발생한다.")
    void throwExceptionWhenDuplicateThemeName() {
        jdbcTemplate.update("INSERT INTO theme (id, name, thumbnail_url, description, status) VALUES (1, '공포의 저택', 'url', '설명', 'AVAILABLE')");

        assertThatThrownBy(() -> themeCommandService.create("공포의 저택", "url", "설명"))
                .isExactlyInstanceOf(ConflictException.class)
                .hasMessage(ErrorMessage.DUPLICATE_THEME.getMessage());
    }
}
