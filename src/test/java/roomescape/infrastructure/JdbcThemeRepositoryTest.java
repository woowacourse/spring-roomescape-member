package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;

@SpringBootTest
@Transactional
class JdbcThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("테마 정보를 DB에 저장한다.")
    @Test
    void save() {
        Theme theme = new Theme("방탈출", "방탈출을 한다.", "https://url");
        Theme actual = themeRepository.save(theme);
        Theme expected = new Theme(actual.getId(), theme.getName(), theme.getDescription(), theme.getThumbnail());
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("전체 테마 정보를 불러온다.")
    @Test
    void findAll() {
        Theme theme1 = new Theme("방탈출1", "방탈출1을 한다.", "https://url1");
        Theme theme2 = new Theme("방탈출2", "방탈출2을 한다.", "https://url2");

        Theme saved1 = themeRepository.save(theme1);
        Theme saved2 = themeRepository.save(theme2);

        assertThat(themeRepository.findAll()).containsExactlyInAnyOrder(saved1, saved2);
    }

    @DisplayName("ID를 받아와 테마를 DB에서 삭제한다.")
    @Test
    void deleteById() {
        Theme theme1 = new Theme("방탈출1", "방탈출1을 한다.", "https://url1");
        Theme theme2 = new Theme("방탈출2", "방탈출2을 한다.", "https://url2");

        Theme saved1 = themeRepository.save(theme1);
        Theme saved2 = themeRepository.save(theme2);

        themeRepository.deleteById(saved1.getId());

        assertThat(themeRepository.findAll()).hasSize(1);
    }

    @DisplayName("테마 id로부터 테마를 찾는다.")
    @Test
    void findById() {
        Theme theme = new Theme("방탈출1", "방탈출1을 한다.", "https://url1");
        Theme saved = themeRepository.save(theme);
        assertThat(themeRepository.findById(saved.getId())).isEqualTo(saved);
    }

    @DisplayName("테마 이름을 받아서 개수를 센다.")
    @Test
    void isNameExists() {
        String name = "방탈출1";
        Theme theme = new Theme(name, "방탈출1을 한다.", "https://url1");
        themeRepository.save(theme);
        assertThat(themeRepository.isNameExists(name)).isTrue();
    }

    @DisplayName("최근 일주일 내의 인기 테마를 찾는다.")
    @Test
    @Sql("/popularTestData.sql")
    void findPopular() {
        List<Theme> popular = themeRepository.findPopular(10);

        List<Theme> expected = List.of(
            new Theme(1L, "theme1", "desc1", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
            new Theme(3L, "theme3", "desc3", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
            new Theme(2L, "theme2", "desc2", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
        );

        assertThat(popular).containsExactlyElementsOf(expected);
    }
}
