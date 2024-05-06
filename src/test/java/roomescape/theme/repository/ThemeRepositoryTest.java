package roomescape.theme.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.theme.domain.Theme;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/data.sql", "/sample.sql"})
class ThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    void findPopular() {
        List<Theme> themeList = themeRepository.findPopular("2024-04-24", "2024-04-30");
        List<Long> actual = themeList.stream().map(Theme::getId).toList();
        List<Long> expected = List.of(2L, 1L, 3L);

        assertThat(actual).isEqualTo(expected);
    }
}