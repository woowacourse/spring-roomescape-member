package roomescape.theme;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest(properties = "spring.sql.init.mode=never")
@Import(ThemeDao.class)
@Sql(scripts = "classpath:schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:reset-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeDaoTest {
    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 테마를_저장하고_ID로_조회할_수_있다() {
        Theme saved = themeDao.save("Theme A", "desc", "https://example.com/a.png");

        Theme found = themeDao.findById(saved.id()).orElseThrow();

        assertThat(found.id()).isEqualTo(saved.id());
        assertThat(found.name()).isEqualTo("Theme A");
    }

    @Test
    void 테마를_전체_조회할_수_있다() {
        themeDao.save("Theme A", "desc", "https://example.com/a.png");
        themeDao.save("Theme B", "desc", "https://example.com/b.png");

        List<Theme> themes = themeDao.findAll();

        assertThat(themes).hasSize(2);
        assertThat(themes)
                .extracting(Theme::name)
                .containsExactly("Theme A", "Theme B");
    }

    @Test
    void 테마를_삭제할_수_있다() {
        Theme saved = themeDao.save("Theme A", "desc", "https://example.com/a.png");

        themeDao.delete(saved.id());

        assertThat(themeDao.findAll()).isEmpty();
    }

    @Test
    void 예약_수_기준으로_테마를_조회할_수_있다() {
        Theme themeA = themeDao.save("Theme A", "desc", "https://example.com/a.png");
        Theme themeB = themeDao.save("Theme B", "desc", "https://example.com/b.png");
        Theme themeC = themeDao.save("Theme C", "desc", "https://example.com/c.png");
        Theme themeD = themeDao.save("Theme D", "desc", "https://example.com/d.png");

        long time1 = insertTime(LocalTime.parse("10:00:00"));
        long time2 = insertTime(LocalTime.parse("11:00:00"));
        long time3 = insertTime(LocalTime.parse("12:00:00"));
        long time4 = insertTime(LocalTime.parse("13:00:00"));

        LocalDate date1 = LocalDate.of(2026, 5, 1);
        LocalDate date2 = LocalDate.of(2026, 5, 2);
        LocalDate date3 = LocalDate.of(2026, 5, 3);
        LocalDate date4 = LocalDate.of(2026, 5, 4);

        insertReservation(themeA.id(), time1, date1, "User1");
        insertReservation(themeA.id(), time2, date2, "User2");
        insertReservation(themeA.id(), time3, date3, "User3");
        insertReservation(themeA.id(), time4, date4, "User4");

        insertReservation(themeB.id(), time1, date1, "User5");
        insertReservation(themeB.id(), time2, date2, "User6");
        insertReservation(themeB.id(), time3, date3, "User7");

        insertReservation(themeC.id(), time1, date1, "User8");
        insertReservation(themeC.id(), time2, date2, "User9");

        insertReservation(themeD.id(), time1, date1, "User10");

        List<Theme> ranked = themeDao.findRanked("reservationCount", "DESC", LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 6), 10L);

        assertThat(ranked).hasSize(4);
        assertThat(ranked.get(0).name()).isEqualTo("Theme A");
        assertThat(ranked.get(1).name()).isEqualTo("Theme B");
        assertThat(ranked.get(2).name()).isEqualTo("Theme C");
        assertThat(ranked.get(3).name()).isEqualTo("Theme D");
    }

    private long insertTime(LocalTime startAt) {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", startAt);
        return jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_at = ?",
                Long.class,
                startAt
        );
    }

    private void insertReservation(long themeId, long timeId, LocalDate date, String name) {
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                name,
                date,
                timeId,
                themeId
        );
    }
}
