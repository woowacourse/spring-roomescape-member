package roomescape.repository.theme;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.repository.reservation.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.InitialDataFixture.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class ThemeH2RepositoryTest {

    @Autowired
    private ThemeH2Repository themeH2Repository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Theme를 저장한다.")
    void save() {
        Theme theme = new Theme(null, new Name("레벨2"), "레벨2 설명", "레벨2 썸네일");

        Theme saved = themeH2Repository.save(theme);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("id에 맞는 Theme을 제거한다.")
    void delete() {
        Integer before = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme", Integer.class);

        themeH2Repository.delete(THEME_2.getId());

        Integer after = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme", Integer.class);

        assertThat(before - after).isEqualTo(1);
    }

    @Test
    @DisplayName("참조되어 있는 테마를 삭제하는 경우 예외가 발생한다.")
    void deleteReferencedTheme() {
        assertThatThrownBy(() -> themeH2Repository.delete(THEME_1.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("최근 인기 테마를 가져온다.")
    void findPopularThemes() {
        reservationRepository.save(new Reservation(USER_1, LocalDate.now().minusDays(1), RESERVATION_TIME_1, THEME_5));
        reservationRepository.save(new Reservation(USER_1, LocalDate.now().minusDays(1), RESERVATION_TIME_2, THEME_5));
        reservationRepository.save(new Reservation(USER_1, LocalDate.now().minusDays(1), RESERVATION_TIME_3, THEME_5));
        reservationRepository.save(new Reservation(USER_1, LocalDate.now().minusDays(1), RESERVATION_TIME_2, THEME_4));

        List<Theme> popularTheme = themeH2Repository.findPopularThemes(3, 10);

        assertThat(popularTheme.get(0)).isEqualTo(THEME_5);
        assertThat(popularTheme.get(1)).isEqualTo(THEME_4);
    }

    @Test
    @DisplayName("모든 theme를 찾는다.")
    void findAll() {
        List<Theme> found = themeH2Repository.findAll();

        assertThat(found).hasSize(6);
    }

    @Test
    @DisplayName("id에 맞는 theme를 찾는다.")
    void findBy() {
        Theme found = themeH2Repository.findById(THEME_1.getId()).get();

        assertThat(found.getName()).isEqualTo(THEME_1.getName());
    }

    @Test
    @DisplayName("존재하지 않는 id가 들어오면 빈 Optional 객체를 반환한다.")
    void findEmpty() {
        Optional<Theme> theme = themeH2Repository.findById(-1L);

        assertThat(theme.isEmpty()).isTrue();
    }
}
