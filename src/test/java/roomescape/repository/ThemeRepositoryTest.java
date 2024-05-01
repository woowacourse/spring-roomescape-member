package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.theme.Theme;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// TODO: JdbcTest로 변경
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class ThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("데이터베이스에서 모든 테마 정보를 조회한다.")
    void findAllThemes() {
        List<Theme> themes = themeRepository.findAll();

        assertThat(themes.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("데이터베이스에 테마 정보를 추가한다.")
    void saveTheme() {
        Theme theme = themeRepository.save(new Theme("name", "description", "thumbnail"));

        assertThat(theme.getId()).isEqualTo(1);
    }


    @Test
    @DisplayName("테마 id를 통해 데이터베이스에서 테마를 삭제한다.")
    void deleteTheme() {
        Theme theme = themeRepository.save(new Theme("name", "description", "thumbnail"));

        int deleteCount = themeRepository.delete(theme.getId());
        assertThat(deleteCount).isEqualTo(1);
    }
}