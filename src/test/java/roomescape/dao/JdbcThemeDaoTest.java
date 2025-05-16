package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.theme.dao.ThemeDao;
import roomescape.domain.theme.model.Theme;

@Sql(scripts = {"/test-schema.sql"})
@SpringBootTest
public class JdbcThemeDaoTest {

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void clearResourceAfterTest() {
        jdbcTemplate.update("DELETE FROM RESERVATION");
        jdbcTemplate.update("DELETE FROM RESERVATION_TIME");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("DELETE FROM MEMBER");
    }

    @DisplayName("테마 저장 테스트")
    @Test
    void save1() {
        //given
        Theme theme = new Theme("테마1", "공포 테마라 무섭습니다.", "http://aaa");

        //when
        long savedId = themeDao.save(theme);
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
        themeDao.save(theme1);
        themeDao.save(theme2);
        themeDao.save(theme3);

        //then
        assertThat(themeDao.findAll().size()).isEqualTo(3);
    }

    @DisplayName("테마 아이디가 주어졌을 때, 해당되는 테마를 삭제할 수 있어야 한다.")
    @Test
    void given_theme_id_then_remote_theme() {
        //given
        Theme theme1 = new Theme("테마1", "공포 테마라 무섭습니다.", "http://aaa1");
        long savedId = themeDao.save(theme1);

        //when
        themeDao.delete(savedId);
        Optional<Theme> findTheme = themeDao.findById(savedId);

        //then
        assertThat(themeDao.findAll().size()).isEqualTo(0);
        assertThat(findTheme).isEmpty();
    }

    @DisplayName("주어진 날짜를 기준으로 7일 동안 가장 많이 예약된 테마 순서대로 조회할 수 있어야 한다.")
    @Test
    void find_popular_rank() {
        initData();
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        List<Theme> allThemeOfRanks = themeDao.calculateRankForReservationAmount(
            startDate.minusDays(7),
            startDate);
        assertThat(allThemeOfRanks.get(0).getId()).isEqualTo(1);
        assertThat(allThemeOfRanks.get(1).getId()).isEqualTo(2);
        assertThat(allThemeOfRanks.get(2).getId()).isEqualTo(3);
    }

    private void initData() {
        String sql1 = "INSERT INTO theme (id, name, description, thumbnail) VALUES (1, '안녕, 자두야', '자두', 'https://jado.com');";
        String sql2 = "INSERT INTO theme (id, name, description, thumbnail) VALUES (2, '안녕, 도기야', '도가', 'https://dogi.com');";
        String sql3 = "INSERT INTO theme (id, name, description, thumbnail)VALUES (3, '안녕, 젠슨아', '젠슨', 'https://jenson.com');";
        String sql4 = "INSERT INTO reservation_time (id, start_at) VALUES (1, '10:00');";
        String sql5 = "INSERT INTO reservation_time (id, start_at) VALUES (2, '11:00');";
        String sql6 = "INSERT INTO reservation_time (id, start_at) VALUES (3, '12:00');";
        String sql7 = "INSERT INTO reservation_time (id, start_at) VALUES (4, '13:00');";
        String sql8 = "INSERT INTO member (id, role, name, email, password) VALUES (1, 'admin', 'jenson', 'a@example.com', 'abc');";
        String sql9 = "INSERT INTO reservation (date, member_id, time_id, theme_id) VALUES ('2025-04-24', 1, 1, 1);";
        String sql10 = "INSERT INTO reservation (date, member_id, time_id, theme_id)VALUES ('2025-04-25', 1, 2, 1);";
        String sql11 = "INSERT INTO reservation (date, member_id, time_id, theme_id)VALUES ('2025-04-26', 1, 3, 1);";
        String sql12 = "INSERT INTO reservation (date, member_id, time_id, theme_id)VALUES ('2025-04-27', 1, 1, 2);";
        String sql13 = "INSERT INTO reservation (date, member_id, time_id, theme_id)VALUES ('2025-04-28', 1, 1, 2);";
        String sql14 = "INSERT INTO reservation (date, member_id, time_id, theme_id)VALUES ('2025-05-01', 1, 2, 3);";
        jdbcTemplate.update(sql1);
        jdbcTemplate.update(sql2);
        jdbcTemplate.update(sql3);
        jdbcTemplate.update(sql4);
        jdbcTemplate.update(sql5);
        jdbcTemplate.update(sql6);
        jdbcTemplate.update(sql7);
        jdbcTemplate.update(sql8);
        jdbcTemplate.update(sql9);
        jdbcTemplate.update(sql10);
        jdbcTemplate.update(sql11);
        jdbcTemplate.update(sql12);
        jdbcTemplate.update(sql13);
        jdbcTemplate.update(sql14);
    }

}
