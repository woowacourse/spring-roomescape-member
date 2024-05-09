package roomescape.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void findThemesOrderByReservationThemeCountDesc() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate sevenDaysBefore = LocalDate.now().minusDays(7);
        for (int i = 2; i <= 12; i++) {
            jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름" + i, "설명", "썸네일");
            jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)", 1L, yesterday, 1, i);
            jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)", 1L, sevenDaysBefore, 1, i);
        }
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름13", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)", 1L, yesterday, 1, 13);

        List<Theme> themesByDescOrder = themeRepository.findBest10Between(sevenDaysBefore, yesterday);
        assertThat(themesByDescOrder).doesNotContain(new Theme(13L, "이름13", "설명", "썸네일"));
    }
}
