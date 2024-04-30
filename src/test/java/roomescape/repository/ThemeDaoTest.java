package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Theme;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class ThemeDaoTest {

    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeDaoTest(JdbcTemplate jdbcTemplate) {
        this.themeRepository = new ThemeDao(jdbcTemplate);
    }

    @DisplayName("테마 DAO는 생성 요청이 들어오면 DB에 값을 저장한다.")
    @Test
    void save() {
        // given
        Theme theme = new Theme(
                "공포",
                "완전 무서운 테마",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        // when
        Theme savedTheme = themeRepository.save(theme);
        Optional<Theme> actual = themeRepository.findById(savedTheme.getId());

        // then
        assertThat(actual.isPresent()).isTrue();
    }
}
