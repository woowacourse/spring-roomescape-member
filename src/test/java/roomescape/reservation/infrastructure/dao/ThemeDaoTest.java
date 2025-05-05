package roomescape.reservation.infrastructure.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.presentation.dto.ThemeRequest;

@JdbcTest
public class ThemeDaoTest {

    private final ThemeDao themeDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ThemeDaoTest(JdbcTemplate jdbcTemplate) {
        this.themeDao = new ThemeDao(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Test
    @DisplayName("테마 추가 확인 테스트")
    void insertTest() {
        // given
        ThemeRequest themeRequest = new ThemeRequest(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        // when
        themeDao.insert(themeRequest);

        // then
        assertThat(count()).isEqualTo(1);
    }

    @Test
    @DisplayName("테마 전체 조회 확인 테스트")
    void getThemesTest() {
        // given
        ThemeRequest themeRequest = new ThemeRequest(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        themeDao.insert(themeRequest);

        // when
        List<Theme> allThemes = themeDao.findAllThemes();

        // then
        assertThat(allThemes).hasSize(1);
    }

    @Test
    @DisplayName("테마 삭제 확인 테스트")
    void deleteTest() {
        // when
        themeDao.delete(0L);

        // then
        assertThat(count()).isEqualTo(0);
    }

    private int count() {
        String sql = "select count(*) from theme";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
