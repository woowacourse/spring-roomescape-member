package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.context.annotation.Import;
import roomescape.domain.Theme;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@JdbcTest
@Import(JdbcThemeRepository.class)
class JdbcThemeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcThemeRepository jdbcThemeRepository;

    @Test
    @DisplayName("모든 Theme를 불러온다.")
    public void findAll() {
        // given
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "kim", "desc1", "thumb1"
        );
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "lee", "desc2", "thumb2"
        );
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "park", "desc3", "thumb3"
        );

        // when
        List<Theme> themes = jdbcThemeRepository.findAll();

        // then
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


}
