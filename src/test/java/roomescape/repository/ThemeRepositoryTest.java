package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(scripts = {"/sample.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"/drop.sql", "/schema.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@JdbcTest
class ThemeRepositoryTest {

    private final ThemeRepository themeRepository;

    @Autowired
    ThemeRepositoryTest(final DataSource dataSource) {
        this.themeRepository = new H2ThemeRepository(dataSource);
    }

    @Test
    @DisplayName("모든 테마 목록을 조회한다.")
    void findAll() {
        // given
        final List<Theme> expected = List.of(
                new Theme(1L, "spring", "Escape from spring cold", "Spring thumb"),
                new Theme(2L, "summer", "Escape from hottest weather", "Summer thumb")
        );

        // when
        final List<Theme> actual = themeRepository.findAll();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}
