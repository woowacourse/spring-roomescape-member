package roomescape.reservation.infra;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class JdbcThemeRepositoryTest {
    @Autowired
    private JdbcThemeRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    void 테마_저장_레포지토리_테스트() {
        Theme theme = new Theme(null, "무서운게 딱 좋아", "무서운 분위기의 방탈출", "https://example.com/theme.jpg");

        Theme savedTheme = repository.save(theme);
        Long id = jdbcTemplate.queryForObject("SELECT id FROM theme LIMIT 1", Long.class);

        assertThat(savedTheme.getId()).isEqualTo(id);
        assertThat(savedTheme.getName()).isEqualTo("공포");
        assertThat(savedTheme.getDescription()).isEqualTo("무서운 분위기의 방탈출");
        assertThat(savedTheme.getThumbnailUrl()).isEqualTo("https://example.com/theme.jpg");
    }
}
