package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.exception.DuplicateException;
import roomescape.exception.ResourceInUseException;
import roomescape.theme.domain.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class AdminThemeServiceTest {

    @Autowired
    private AdminThemeService adminThemeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("DELETE FROM reservation");
        jdbcTemplate.execute("DELETE FROM reservation_time");
        jdbcTemplate.execute("DELETE FROM themes");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
        jdbcTemplate.execute("ALTER TABLE themes ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");

        // Theme A(id=1) — 예약 있음, Theme E(id=5) — 예약 없음
        jdbcTemplate.update("INSERT INTO themes (name, description, thumbnail) VALUES ('Theme A', 'Desc', 'https://a.png')");
        jdbcTemplate.update("INSERT INTO themes (name, description, thumbnail) VALUES ('Theme B', 'Desc', 'https://b.png')");
        jdbcTemplate.update("INSERT INTO themes (name, description, thumbnail) VALUES ('Theme C', 'Desc', 'https://c.png')");
        jdbcTemplate.update("INSERT INTO themes (name, description, thumbnail) VALUES ('Theme D', 'Desc', 'https://d.png')");
        jdbcTemplate.update("INSERT INTO themes (name, description, thumbnail) VALUES ('Theme E', 'Desc', 'https://e.png')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00:00')");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES ('User1', '2026-05-01', 1, 1)");
    }

    @Test
    void 테마를_추가할_수_있다() {
        Theme saved = adminThemeService.save("New Theme", "설명", "https://example.com/thumb.png");

        assertThat(saved.name()).isEqualTo("New Theme");
        assertThat(saved.id()).isNotNull();
    }

    @Test
    void 테마가_중복되면_예외가_발생한다() {
        assertThatThrownBy(() -> adminThemeService.save("Theme A", "desc", "thumb"))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("같은 이름의 테마가 존재합니다.");
    }

    @Test
    void 예약이_있으면_테마를_삭제할_수_없다() {
        assertThatThrownBy(() -> adminThemeService.delete(1L))
                .isInstanceOf(ResourceInUseException.class)
                .hasMessage("예약이 있어 삭제할 수 없습니다");
    }

    @Test
    void 예약이_없으면_테마를_삭제할_수_있다() {
        assertThatCode(() -> adminThemeService.delete(5L))
                .doesNotThrowAnyException();
    }
}