package roomescape.domain;

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

@JdbcTest
@Sql(scripts = "/reset_test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
public class ThemeRepositoryTest {
    private ThemeRepository themeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        themeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("모든 테마를 조회한다.")
    void findAll() {
        // when
        List<Theme> themes = themeRepository.findAll();

        // then
        assertThat(themes).hasSize(2);
    }

    @Test
    @DisplayName("테마를 생성한다.")
    void create() {
        // given
        Theme theme = new Theme("레벨4 탈출",
                "우테코 레벨4를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        // when
        themeRepository.create(theme);

        // then
        List<Theme> themes = themeRepository.findAll();
        assertThat(themes).hasSize(3);
    }
}
