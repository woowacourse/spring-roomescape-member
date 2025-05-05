package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;

@Sql(scripts = {"/test-schema.sql"})
@SpringBootTest
public class JdbcThemeDaoTest {

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void clearResourceAfterTest() {
        jdbcTemplate.update("DELETE FROM theme");

    }

    @DisplayName("테마 저장 테스트")
    @Test
    void save1() {
        //given
        Theme theme = new Theme("테마1", "공포 테마라 무섭습니다.", "http://aaa");

        //when
        long savedId = themeDao.saveTheme(theme);
        //then

        Theme findTheme = themeDao.findById(savedId).get();
        assertThat(findTheme.getName()).isEqualTo("테마1");
        assertThat(findTheme.getDescription()).isEqualTo("공포 테마라 무섭습니다.");
        assertThat(findTheme.getThumbnail()).isEqualTo("http://aaa");
    }

    @DisplayName("테마 조회 테스트")
    @Test
    void get_all_theme() {
        //given
        Theme theme1 = new Theme("테마1", "공포 테마라 무섭습니다.", "http://aaa1");
        Theme theme2 = new Theme("테마2", "공포 테마라 무섭습니다3.", "http://aaa2");
        Theme theme3 = new Theme("테마3", "공포 테마라 무섭습니다.", "http://aaa3");

        //when
        themeDao.saveTheme(theme1);
        themeDao.saveTheme(theme2);
        themeDao.saveTheme(theme3);

        //then
        assertThat(themeDao.findAllTheme().size()).isEqualTo(3);
    }

    @DisplayName("테마 아이디가 주어졌을 때, 해당되는 테마를 삭제할 수 있어야 한다.")
    @Test
    void given_theme_id_then_remote_theme() {
        //given
        Theme theme1 = new Theme("테마1", "공포 테마라 무섭습니다.", "http://aaa1");
        long savedId = themeDao.saveTheme(theme1);

        //when
        themeDao.deleteTheme(savedId);
        Optional<Theme> findTheme = themeDao.findById(savedId);

        //then
        assertThat(themeDao.findAllTheme().size()).isEqualTo(0);
        assertThat(findTheme).isEmpty();
    }

    // TODO : 테스트 코드 작성하기 / 다른 DAO와 연관관계가 있어 패스
    @DisplayName("주어진 날짜를 기준으로 7일 동안 가장 많이 예약된 테마 순서대로 조회할 수 있어야 한다.")
    void find_popular_rank() {

    }
}
