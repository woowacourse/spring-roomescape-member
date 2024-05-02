package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;

@JdbcTest
@Import({ThemeRepository.class, ReservationTimeRepository.class, ReservationRepository.class})
class ThemeRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("id 로 엔티티를 찾는다.")
    void findByIdTest() {
        Theme theme = new Theme(new Name("공포"), "무서운 테마", "https://i.pinimg.com/236x.jpg");
        Long themeId = themeRepository.save(theme);
        Theme findTheme = themeRepository.findById(themeId).get();

        assertThat(findTheme.getId()).isEqualTo(themeId);
    }

    @Test
    @DisplayName("전체 엔티티를 조회한다.")
    void findAllTest() {
        Theme theme1 = new Theme(new Name("공포"), "무서운 테마", "https://i.pinimg.com/236x.jpg");
        Theme theme2 = new Theme(new Name("SF"), "미래 테마", "https://i.pinimg.com/123x.jpg");
        themeRepository.save(theme1);
        themeRepository.save(theme2);
        List<Theme> themes = themeRepository.findAll();

        assertThat(themes.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("예약이 많은 순으로 10개의 테마를 조회한다.")
    void findPopularThemeLimitTen() {
        Long timeId = reservationTimeRepository.save(new ReservationTime(LocalTime.now()));
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId).get();

        Long theme1Id = themeRepository.save(new Theme(new Name("공포"), "a", "a"));
        Theme theme1 = themeRepository.findById(theme1Id).get();

        Long theme2Id = themeRepository.save(new Theme(new Name("액션"), "b", "b"));
        Theme theme2 = themeRepository.findById(theme2Id).get();

        reservationRepository.save(
                new Reservation(new Name("hogi"), LocalDate.parse("2024-12-12"), theme1, reservationTime));
        reservationRepository.save(
                new Reservation(new Name("kaki"), LocalDate.parse("2024-05-05"), theme2, reservationTime));
        reservationRepository.save(
                new Reservation(new Name("neo"), LocalDate.parse("2024-05-06"), theme2, reservationTime));

        List<Theme> themes = themeRepository.findPopularThemeLimitTen();

        assertAll(
                () -> assertThat(themes.get(0).getName()).isEqualTo("액션"),
                () -> assertThat(themes.size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("id를 받아 삭제한다.")
    void deleteTest() {
        Theme theme = new Theme(new Name("공포"), "무서운 테마", "https://i.pinimg.com/236x.jpg");
        Long themeId = themeRepository.save(theme);
        themeRepository.delete(themeId);
        List<Theme> themes = themeRepository.findAll();

        assertThat(themes.size()).isEqualTo(0);
    }
}
