package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Theme;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@JdbcTest
@Import(JdbcThemeRepository.class)
class JdbcThemeRepositoryTest {

    @Autowired
    private JdbcThemeRepository jdbcThemeRepository;

    @Test
    @DisplayName("모든 Theme를 불러온다.")
    public void findAll() {
        jdbcThemeRepository.save(new Theme("kim", "desc1", "thumb1"));
        jdbcThemeRepository.save(new Theme("lee", "desc2", "thumb2"));
        jdbcThemeRepository.save(new Theme("park", "desc3", "thumb3"));

        List<Theme> themes = jdbcThemeRepository.findAll();

        assertThat(themes).hasSize(3)
                .extracting(
                        Theme::getName,
                        Theme::getDescription,
                        Theme::getThumbnail
                ).containsExactlyInAnyOrder(
                        tuple("kim", "desc1", "thumb1"),
                        tuple("lee", "desc2", "thumb2"),
                        tuple("park", "desc3", "thumb3")
                );
    }

    @Test
    @DisplayName("Theme를 저장하고 조회한다.")
    public void saveAndFindById() {
        Theme theme = jdbcThemeRepository.save(new Theme("kim", "desc1", "thumb1"));

        Optional<Theme> found = jdbcThemeRepository.findById(theme.getId());

        assertThat(found).isPresent();
        Theme savedTheme = found.get();
        assertThat(savedTheme.getId()).isEqualTo(theme.getId());
        assertThat(savedTheme.getName()).isEqualTo("kim");
        assertThat(savedTheme.getDescription()).isEqualTo("desc1");
        assertThat(savedTheme.getThumbnail()).isEqualTo("thumb1");
    }

    @Test
    @DisplayName("Theme를 삭제한다.")
    public void deleteById() {
        Theme theme = jdbcThemeRepository.save(new Theme("kim", "desc1", "thumb1"));

        jdbcThemeRepository.deleteById(theme.getId());

        assertThat(jdbcThemeRepository.findAll()).isEmpty();
    }

}
