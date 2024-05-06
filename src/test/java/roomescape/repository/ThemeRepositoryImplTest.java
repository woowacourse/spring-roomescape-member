package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Theme;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeRepositoryImplTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into theme (name, description, thumbnail) values('dodo', 'dodododo', 'url')");
    }

    @AfterEach
    void setDown() {
        jdbcTemplate.update("delete from theme");
    }

    @DisplayName("테마 전체를 조회할 수 있습니다")
    @Test
    void should_find_all() {
        assertThat(themeRepository.findAll()).hasSize(1);
    }

    @DisplayName("테마를 추가할 수 있습니다")
    @Test
    void should_insert_theme() {
        Theme theme = new Theme(null, "리비", "멋짐", "url");
        Theme inserted = themeRepository.insert(theme);

        assertThat(inserted.getId()).isNotNull();
    }

    @DisplayName("원하는 ID의 테마를 삭제할 수 있습니다")
    @Test
    void should_deleteById() {
        themeRepository.deleteById(1L);
        int count = jdbcTemplate.queryForObject("select count(*) from reservation_time where id = 1", Integer.class);

        assertThat(count).isZero();
    }
}
