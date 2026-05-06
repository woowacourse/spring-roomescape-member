package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class JdbcThemeRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    JdbcThemeRepository repository;

    @Autowired
    public JdbcThemeRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.repository = new JdbcThemeRepository(jdbcTemplate);
    }

    @DisplayName("해당 시간이 존재하는지 조회한다.")
    @Test
    void existsByNameTest() {
        // given
        jdbcTemplate.update("insert into theme(id, name, description, thumbnail_url) values (1, '테마', '설명', 'url')");

        // when
        boolean exists = repository.existsByName("테마");
        boolean notExists = repository.existsByName("other");

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
