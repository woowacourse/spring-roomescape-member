package roomescape.domain.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.theme.Theme;

@JdbcTest
class JdbcThemeRepositoryTest {
    private final ThemeRepository themeRepository;

    @Autowired
    JdbcThemeRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.themeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    void 테마를_저장한다() {
        Theme theme = new Theme("테마1", "설명", "썸네일1");

        Theme savedTheme = themeRepository.save(theme);

        assertAll(
                () -> assertThat(savedTheme.getName()).isEqualTo("테마1"),
                () -> assertThat(savedTheme.getDescription()).isEqualTo("설명"),
                () -> assertThat(savedTheme.getThumbnail()).isEqualTo("썸네일1")
        );
    }

    @Test
    void 모든_테마를_조회한다() {
        Theme theme1 = new Theme("테마1", "설명", "썸네일1");
        Theme theme2 = new Theme("테마2", "설명", "썸네일2");
        themeRepository.save(theme1);
        themeRepository.save(theme2);

        List<Theme> themes = themeRepository.findAll();
        assertAll(
                () -> assertThat(themes).hasSize(2),
                () -> assertThat(themes.get(0).getName()).isEqualTo("테마1"),
                () -> assertThat(themes.get(1).getName()).isEqualTo("테마2")
        );
    }

    @Test
    void 테마를_삭제한다() {
        Theme theme = new Theme("테마1", "설명", "썸네일1");
        theme = themeRepository.save(theme);

        themeRepository.deleteById(theme.getId());

        List<Theme> themes = themeRepository.findAll();
        assertThat(themes).isEmpty();
    }

    @Test
    void 테마_이름으로_테마를_찾는다() {
        Theme theme = new Theme("테마1", "설명", "썸네일1");
        theme = themeRepository.save(theme);

        assertThat(themeRepository.existsByName("테마1")).isTrue();
    }

    @Test
    @Sql("/data1.sql")
    void 예약이_많은_순서대로_최대_10개_테마를_반환한다() {
        List<Theme> allByRank = themeRepository.findAllByRank(LocalDate.now());
        assertAll(
                () -> assertThat(allByRank).hasSize(3),
                () -> assertThat(allByRank.get(0).getId()).isEqualTo(1)
        );
    }
}
