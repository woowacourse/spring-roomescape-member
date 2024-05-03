package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = {"/drop.sql", "/schema.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Import(H2ThemeRepository.class)
@JdbcTest
class ThemeRepositoryTest {

    final long LAST_ID = 4;
    final Theme exampleFirstTheme = new Theme(
            1L,
            "Spring",
            "A time of renewal and growth, as nature awakens from its slumber and bursts forth with vibrant colors and fragrant blooms.",
            "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
    );

    @Autowired
    ThemeRepository themeRepository;

    @Test
    @DisplayName("모든 테마 목록을 조회한다.")
    void findAll() {
        // given & when
        final List<Theme> actual = themeRepository.findAll();

        // then
        assertThat(actual).hasSize((int) LAST_ID);
        assertThat(actual.get(0)).isEqualTo(exampleFirstTheme);
    }
}
