package roomescape.repository.theme;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.*;
import roomescape.repository.member.MemberRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;

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
    private ReservationRepository reservationRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("Theme를 저장한다.")
    void save() {
        Theme theme = new Theme(null, new Name("레벨2"), "레벨2 설명", "레벨2 썸네일");

        Theme saved = themeRepository.save(theme);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("id에 맞는 Theme을 제거한다.")
    void delete() {
        Theme theme = themeRepository.save(THEME_2);
        Integer before = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme", Integer.class);

        themeRepository.delete(theme.getId());

        Integer after = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme", Integer.class);

        assertThat(before - after).isEqualTo(1);
    }

    @Test
    @DisplayName("참조되어 있는 테마를 삭제하는 경우 예외가 발생한다.")
    void deleteReferencedTheme() {
        Member member = memberRepository.save(USER_1);
        Theme theme = themeRepository.save(THEME_1);
        ReservationTime time = reservationTimeRepository.save(RESERVATION_TIME_1);
        reservationRepository.save(new Reservation(member, LocalDate.now().plusDays(1), time, theme));

        assertThatThrownBy(() -> themeRepository.delete(theme.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("최근 인기 테마를 가져온다.")
    void findPopularThemes() {
        Member member = memberRepository.save(USER_1);
        Theme theme5 = themeRepository.save(THEME_5);
        Theme theme4 = themeRepository.save(THEME_4);
        ReservationTime time = reservationTimeRepository.save(RESERVATION_TIME_1);

        reservationRepository.save(new Reservation(member, LocalDate.now().minusDays(1), time, theme5));
        reservationRepository.save(new Reservation(member, LocalDate.now().minusDays(1), time, theme5));
        reservationRepository.save(new Reservation(member, LocalDate.now().minusDays(1), time, theme5));
        reservationRepository.save(new Reservation(member, LocalDate.now().minusDays(1), time, theme4));
        reservationRepository.save(new Reservation(member, LocalDate.now().minusDays(1), time, theme4));

        List<Theme> popularTheme = themeRepository.findPopularThemes(3, 10);

        assertThat(popularTheme.get(0)).isEqualTo(theme5);
        assertThat(popularTheme.get(1)).isEqualTo(theme4);
    }

    @Test
    @DisplayName("id에 맞는 theme를 찾는다.")
    void findBy() {
        Theme theme = themeRepository.save(new Theme(null, new Name("myTestTheme"), "asd", "asd"));
        Theme found = themeRepository.findById(theme.getId()).get();

        assertThat(found.getName()).isEqualTo(theme.getName());
    }

    @Test
    @DisplayName("존재하지 않는 id가 들어오면 빈 Optional 객체를 반환한다.")
    void findEmpty() {
        Optional<Theme> theme = themeRepository.findById(-1L);

        assertThat(theme.isEmpty()).isTrue();
    }
}
