package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

import roomescape.model.Theme;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(scripts = {"/reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;


    @DisplayName("테마 목록 조회")
    @Test
    void findAll() {
        final List<Theme> themes = themeRepository.findAll();
        assertThat(themes).hasSize(13);
    }

    @DisplayName("테마 저장")
    @Test
    void save() {
        final Theme theme = themeRepository.save(new Theme("테마테마", "테마 설명", "썸네일썸네일"));
        final Theme savedTheme = themeRepository.findById(theme.getId()).get();

        assertAll(
                () -> assertThat(savedTheme.getName()).isEqualTo("테마테마"),
                () -> assertThat(savedTheme.getDescription()).isEqualTo("테마 설명"),
                () -> assertThat(savedTheme.getThumbnail()).isEqualTo("썸네일썸네일")
        );
    }

    @DisplayName("테마 삭제")
    @Test
    void deleteById() {
        themeRepository.deleteById(13L);
        assertThat(themeRepository.findById(13L)).isEmpty();
    }

    @DisplayName("특정 테마 조회")
    @Test
    void findById() {
        final Theme theme = themeRepository.findById(1L).get();
        assertAll(
                () -> assertThat(theme.getName()).isEqualTo("이름1"),
                () -> assertThat(theme.getDescription()).isEqualTo("설명1"),
                () -> assertThat(theme.getThumbnail()).isEqualTo("https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg")
        );
    }
}
