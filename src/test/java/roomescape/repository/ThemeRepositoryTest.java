package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Theme;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class ThemeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setup() {
        this.themeRepository = new ThemeRepository(jdbcTemplate);
        jdbcTemplate.update("DELETE FROM reservation;");
        jdbcTemplate.update("DELETE FROM theme");
    }

    @Test
    void 테마_추가_테스트() {
        // given
        Theme theme = new Theme(null, "새로운 테마", "새로운 테마 설명", "새로운 썸네일 링크");

        // when
        Long id = themeRepository.insert(theme);

        // then
        List<Theme> themes = themeRepository.findAll();
        Theme savedTheme = themeRepository.findBy(id).get();
        assertAll(
                () -> assertThat(id).isNotNull(),
                () -> assertThat(themes).hasSize(1),
                () -> assertThat(savedTheme.getName()).isEqualTo(theme.getName()));
    }

    @Test
    void 예약_삭제_테스트() {
        // given
        Theme theme1 = new Theme(null, "새로운 테마1", "새로운 테마 설명1", "새로운 썸네일 링크1");
        Theme theme2 = new Theme(null, "새로운 테마2", "새로운 테마 설명2", "새로운 썸네일 링크2");
        Long id1 = themeRepository.insert(theme1);
        Long id2 = themeRepository.insert(theme2);

        // when
        int deletedCount = themeRepository.delete(id1);

        // then
        List<Theme> themes = themeRepository.findAll();
        assertAll(
                () -> assertThat(deletedCount).isEqualTo(1),
                () -> assertThat(themes).hasSize(1),
                () -> assertThat(themeRepository.findBy(id1)).isEmpty());
    }
}
