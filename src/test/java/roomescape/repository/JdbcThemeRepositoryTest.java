package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.JdbcThemeRepository;

@RoomescapeRepositoryTest
class JdbcThemeRepositoryTest {

    private JdbcThemeRepository jdbcThemeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcThemeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("테마 저장")
    void theme_save_test() {
        // given
        Theme theme = Theme.createNew("미술관의 밤", "추리 테마", "https://example.com/theme.png");

        // when
        Theme result = jdbcThemeRepository.save(theme);
        Theme saved = jdbcThemeRepository.findById(result.getId())
                .orElseThrow();

        // then
        assertThat(saved).isEqualTo(result);
    }

    @Test
    @DisplayName("테마 전체 조회")
    void theme_findAll_test() {
        // given
        jdbcThemeRepository.save(Theme.createNew("미술관의 밤", "추리 테마", "https://example.com/theme.png"));

        // when
        List<Theme> themes = jdbcThemeRepository.findAll();

        // then
        assertThat(themes).hasSize(1);
    }

    @Test
    @DisplayName("테마 이름 중복 저장 예외")
    void theme_save_duplicate_test() {
        // given
        jdbcThemeRepository.save(Theme.createNew("미술관의 밤", "추리 테마", "https://example.com/theme.png"));

        // when & then
        assertThrows(DataIntegrityViolationException.class, () ->
                jdbcThemeRepository.save(Theme.createNew("미술관의 밤", "새 설명", "https://example.com/new-theme.png"))
        );
    }

    @Test
    @DisplayName("테마 삭제")
    void theme_delete_test() {
        // given
        Theme saved = jdbcThemeRepository.save(
                Theme.createNew("미술관의 밤", "추리 테마", "https://example.com/theme.png")
        );
        int beforeSize = jdbcThemeRepository.findAll().size();

        // when
        jdbcThemeRepository.deleteById(saved.getId());

        // then
        int afterSize = jdbcThemeRepository.findAll().size();
        assertThat(afterSize).isEqualTo(beforeSize - 1);
    }

    @Test
    @DisplayName("테마 이름 존재 여부 확인")
    void theme_existsByName_test() {
        // given
        jdbcThemeRepository.save(Theme.createNew("미술관의 밤", "추리 테마", "https://example.com/theme.png"));

        // when
        boolean exists = jdbcThemeRepository.existsByName("미술관의 밤");
        boolean notExists = jdbcThemeRepository.existsByName("놀이공원 탈출");

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

}
