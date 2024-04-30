package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Theme;

@JdbcTest
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
        //given
        Theme theme = new Theme("name", "description", "thumbnail");

        //when
        Theme result = themeRepository.save(theme);

        //then
        assertThat(result.getId()).isNotZero();
    }

    @DisplayName("전체 테마 목록을 조회한다.")
    @Test
    void findAll() {
        //given
        themeRepository.save(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        //when
        List<Theme> themes = themeRepository.findAll();

        //then
        assertThat(themes).hasSize(1);
    }
}
