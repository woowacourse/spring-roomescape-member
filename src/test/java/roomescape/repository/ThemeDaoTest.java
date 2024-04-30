package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql(value = {"/recreate_theme.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeDaoTest {

    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeDaoTest(JdbcTemplate jdbcTemplate) {
        this.themeRepository = new ThemeDao(jdbcTemplate);
    }

    @DisplayName("테마 DAO는 생성 요청이 들어오면 DB에 값을 저장한다.")
    @Test
    void save() {
        // given
        Theme theme = new Theme(
                "공포",
                "완전 무서운 테마",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        // when
        Theme savedTheme = themeRepository.save(theme);
        Optional<Theme> actual = themeRepository.findById(savedTheme.getId());

        // then
        assertThat(actual).isPresent();
    }

    @DisplayName("테마 DAO는 조회 요청이 들어오면 DB에 저장된 모든 테마를 반환한다.")
    @Test
    void findAll() {
        // when
        List<Theme> themes = themeRepository.findAll();

        // then
        assertThat(themes).hasSize(2);
    }

    @DisplayName("테마 DAO는 삭제 요청이 들어오면 id에 맞는 값을 삭제한다.")
    @Test
    void delete() {
        // given
        Long id = 1L;

        // when
        themeRepository.delete(id);
        Optional<Theme> actual = themeRepository.findById(id);

        // then
        assertThat(actual).isNotPresent();
    }
}
