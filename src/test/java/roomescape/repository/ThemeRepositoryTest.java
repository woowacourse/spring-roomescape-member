package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.theme.PopularThemeCondition;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeCommand;
import roomescape.domain.theme.ThemeWithCount;
import roomescape.repository.theme.JdbcThemeRepository;
import roomescape.repository.theme.ThemeRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class ThemeRepositoryTest extends BaseRepositoryTest {
    private ThemeRepository themeRepository;

    @Override
    protected void initTable() {
        createThemeTable();
        insertTheme("테마1", "테마 설명", "image url");

        this.themeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Override
    protected void deleteTable() {
        deleteThemeTable();
    }

    @Test
    @DisplayName("특정 예약 테마 정상적으로 가져오는 지 테스트")
    void getThemeTest() {
        Optional<Theme> theme = themeRepository.getTheme(1L);

        assertThat(theme.isPresent()).isTrue();
        assertThat(theme.get()).isEqualTo(new Theme(1, "테마1", "테마 설명", "image url"));
    }

    @Test
    @DisplayName("존재하지 않는 특정 예약 테마 빈 값으로 가져오는 지 테스트")
    void getInvalidThemeTest() {
        Optional<Theme> theme = themeRepository.getTheme(3L);

        assertThat(theme.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("전체 예약 테마 정상적으로 가져오는 지 테스트")
    void getAllThemeTest() {
        List<Theme> reservationTimes = themeRepository.getAllTheme();

        assertThat(reservationTimes).containsExactly(new Theme(1, "테마1", "테마 설명", "image url"));
    }

    @Test
    @DisplayName("예약 테마 삭제 정상적으로 작동하는 지 테스트")
    void deleteReservationTest() {
        themeRepository.deleteTheme(1);
        List<Theme> reservationTimes = themeRepository.getAllTheme();

        assertThat(reservationTimes).isNotIn(new Theme(1, "테마1", "테마 설명", "image url"));
    }

    @Test
    @DisplayName("예약 테마 추가 정상적으로 작동하는 지 테스트")
    void insertReservationTest() {
        Theme theme = themeRepository.addTheme(new ThemeCommand( "테마2", "테마 설명", "image url"));
        List<Theme> reservations = themeRepository.getAllTheme();

        Theme expectedTheme = new Theme(2, "테마2", "테마 설명", "image url");

        assertThat(theme).isEqualTo(expectedTheme);
        assertThat(reservations).contains(expectedTheme);
    }

    @Test
    @DisplayName("특정 기간 인기 테마 정상적으로 가져오는 지 테스트")
    void getPopularThemeTest() {
        createReservationTimeTable();
        createReservationTable();

        String startDate = "2026-04-01";
        String endDate = "2026-05-01";
        long size = 2;

        insertTheme("name1", "description1", "imageUrl1");
        insertTheme("name2", "description2", "imageUrl2");
        insertTheme("name3", "description3", "imageUrl3");

        insertReservationTime(LocalTime.parse("16:29"));

        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)", "reservation1", "2026-04-04", 1, 1, "2026-04-04");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)", "reservation2", "2026-04-04", 1, 2, "2026-04-04");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)", "reservation3", "2026-04-04", 1, 2, "2026-04-04");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)", "reservation3", "2026-04-04", 1, 2, "2026-04-04");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)", "reservation4", "2026-04-04", 1, 3, "2026-04-04");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)", "reservation5", "2026-04-04", 1, 3, "2026-04-04");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id, created_at) VALUES (?, ?, ?, ?, ?)", "reservation6", "2026-05-05", 1, 2, "2026-05-05");

        List<ThemeWithCount> popularThemes = themeRepository.getPopularTheme(new PopularThemeCondition(startDate, endDate, size));
        assertAll(
                () -> assertThat(popularThemes.size()).isEqualTo(2),
                () -> assertThat(popularThemes.getFirst().id()).isEqualTo(2),
                () -> assertThat(popularThemes.getFirst().count()).isEqualTo(3),
                () -> assertThat(popularThemes.getLast().id()).isEqualTo(3),
                () -> assertThat(popularThemes.getLast().count()).isEqualTo(2)
        );

        deleteReservationTable();
        deleteReservationTimeTable();
    }
}
