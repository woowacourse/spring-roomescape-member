package roomescape.reservation.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
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

        assertThat(savedTheme.getName()).isEqualTo("무서운게 딱 좋아");
        assertThat(savedTheme.getDescription()).isEqualTo("무서운 분위기의 방탈출");
        assertThat(savedTheme.getThumbnailUrl()).isEqualTo("https://example.com/theme.jpg");
    }

    @Test
    @Transactional
    void 테마_삭제_레포지토리_테스트() {
        // CASCADE 해결용
        jdbcTemplate.update("DELETE FROM schedule");
        jdbcTemplate.update("DELETE FROM reservation");

        repository.deleteById(1L);
        int rowCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme", Integer.class);

        assertThat(rowCount).isEqualTo(2);
    }

    @Test
    @Transactional
    void 각_날짜에_존재하는_모든_테마_조회_API_테스트(){
        List<Theme> themes = repository.findByDate(LocalDate.of(2026, 5, 5));

        assertThat(themes).hasSize(3);
        assertThat(themes)
                .extracting(Theme::getName)
                .containsExactly("세기의 도둑", "심해 연구소", "시간 여행자");
        assertThat(themes)
                .extracting(Theme::getId)
                .containsExactly(1L, 2L, 3L);
    }
}
