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
}
