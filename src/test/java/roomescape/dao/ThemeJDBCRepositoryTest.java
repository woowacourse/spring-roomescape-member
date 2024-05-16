package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.Theme;

@JdbcTest
@Sql(scripts = "/test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeJDBCRepositoryTest {
    private ThemeRepository themeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        themeRepository = new ThemeJDBCRepository(jdbcTemplate);
    }

    @DisplayName("새로운 테마를 저장한다.")
    @Test
    void saveTheme() {
        //given&when
        Theme theme = themeRepository.save(new Theme("레벨3 탈출", "우테코 레벨3을 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        //then
        assertThat(theme.getId()).isNotZero();
    }

    @DisplayName("전체 테마 목록을 조회한다.")
    @Test
    void findAll() {
        //when
        List<Theme> themes = themeRepository.findAll();

        //then
        assertThat(themes).hasSize(2);
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void deleteTheme() {
        //given
        int expectedSize = 1;

        //when
        themeRepository.deleteById(2);

        //then
        assertThat(themeRepository.findAll()).hasSize(expectedSize);
    }

    @DisplayName("id로 테마를 조회한다.")
    @Test
    void findById() {
        //when
        Theme result = themeRepository.findById(1).get();

        //then
        assertThat(result.getId()).isEqualTo(1);
    }

    @DisplayName("최근 일주일 기준 예약 많은 테마 10개를 조회한다.")
    @Sql(scripts = "/popular_themes_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    void findPopularThemesByOrder() {
        //given
        String name1 = "레벨1 탈출";

        //when
        List<Theme> themes = themeRepository.findByReservationTermAndCount("2024-05-10", "2024-06-04", 10);

        //then
        assertThat(themes.stream().map(Theme::getName).toList()).containsExactly(name1);
    }
}
