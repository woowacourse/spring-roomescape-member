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

@Sql(scripts = {"/drop.sql", "/schema.sql", "/data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
                new Theme(1L, "spring", "Escape from spring cold", "https://img.freepik.com/free-photo/spring-landscape-with-tulips-daisies_123827-29597.jpg?size=626&ext=jpg&ga=GA1.1.553209589.1714348800&semt=sph"),
                new Theme(2L, "summer", "Escape from hottest weather", "https://www.lifewire.com/thmb/unRdFjROthGxGQXL9SZIbc5BzeA=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/summerbeach-5b4650c946e0fb005bfb3207.jpg")
        );

        // when
        final List<Theme> actual = themeRepository.findAll();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }
}
