package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@Sql(value = "/schema.sql", executionPhase = BEFORE_TEST_METHOD)
class ThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("모든 테마 정보를 조회한다.")
    @Test
    void find() {
        // When
        final List<Theme> themes = themeRepository.findAll();

        // Then
        assertThat(themes).hasSize(2);
    }
}
