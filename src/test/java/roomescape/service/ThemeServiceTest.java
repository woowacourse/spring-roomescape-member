package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.ThemeCreateRequest;
import roomescape.dto.ThemeResponse;
import roomescape.exception.ExistReservationException;
import roomescape.exception.IllegalThemeException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void findAllThemes() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "곰세마리", "공포", "푸우");

        // when
        List<ThemeResponse> allThemes = themeService.findAllThemes();

        // then
        assertThat(allThemes).containsExactly(new ThemeResponse(1L, "곰세마리", "공포", "푸우"));
    }

    @DisplayName("중복인 테마를 생성하려고 하면 예외를 발생한다.")
    @Test
    void save_duplicate_IllegalThemeException() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름", "설명", "썸네일");

        // when && then
        assertThatThrownBy(() -> themeService.save(new ThemeCreateRequest(null, "이름", "설명", "썸네일")))
                .isInstanceOf(IllegalThemeException.class);
    }

    @DisplayName("테마를 저장한 아이디를 반환한다.")
    @Test
    void save() {
        // given && when
        long id = themeService.save(new ThemeCreateRequest(null, "이름", "설명", "썸네일"));

        // then
        assertThat(id).isEqualTo(1);
    }

    @DisplayName("예약이 존재하는 경우 테마를 삭제하면 예외가 발생한다.")
    @Test
    void deleteById() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "12:00");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마이름", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니", "2099-07-02", 1, 1);

        // when && then
        assertThatThrownBy(() -> themeService.deleteThemeById(1L))
                .isInstanceOf(ExistReservationException.class);
    }
}
