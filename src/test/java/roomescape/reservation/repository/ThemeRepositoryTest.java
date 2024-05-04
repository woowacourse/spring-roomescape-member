package roomescape.reservation.repository;

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
import roomescape.reservation.domain.Description;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationName;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.ThemeName;

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
        Theme theme = new Theme(new ThemeName("공포"), new Description("무서운 테마"), "https://i.pinimg.com/236x.jpg");
        Long themeId = themeRepository.save(theme);
        Theme findTheme = themeRepository.findById(themeId).get();

        assertThat(findTheme.getId()).isEqualTo(themeId);
    }

    @Test
    @DisplayName("전체 엔티티를 조회한다.")
    void findAllTest() {
        Theme theme1 = new Theme(new ThemeName("공포"), new Description("무서운 테마"), "https://i.pinimg.com/236x.jpg");
        Theme theme2 = new Theme(new ThemeName("SF"), new Description("미래 테마"), "https://i.pinimg.com/123x.jpg");
        themeRepository.save(theme1);
        themeRepository.save(theme2);
        List<Theme> themes = themeRepository.findAll();

        assertThat(themes.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("최근 1주일을 기준하여 예약이 많은 순으로 10개의 테마를 조회한다.")
    void findTopTenThemesDescendingOfLastWeekTest() {
        Long timeId = reservationTimeRepository.save(new ReservationTime(LocalTime.now()));
        ReservationTime reservationTime = reservationTimeRepository.findById(timeId).get();

        Long theme1Id = themeRepository.save(
                new Theme(
                        new ThemeName("공포"),
                        new Description("무서운 테마"),
                        "https://i.pinimg.com/236x.jpg"
                )
        );
        Theme theme1 = themeRepository.findById(theme1Id).get();

        Long theme2Id = themeRepository.save(
                new Theme(
                        new ThemeName("액션"),
                        new Description("액션 테마"),
                        "https://i.pinimg.com/236x.jpg"
                )
        );
        Theme theme2 = themeRepository.findById(theme2Id).get();

        Long theme3Id = themeRepository.save(
                new Theme(
                        new ThemeName("SF"),
                        new Description("미래 테마"),
                        "https://i.pinimg.com/236x.jpg"
                )
        );
        Theme theme3 = themeRepository.findById(theme3Id).get();

        Long theme4Id = themeRepository.save(
                new Theme(
                        new ThemeName("호러"),
                        new Description("호러 테마"),
                        "https://i.pinimg.com/236x.jpg"
                )
        );
        Theme theme4 = themeRepository.findById(theme4Id).get();

        Long theme5Id = themeRepository.save(
                new Theme(
                        new ThemeName("지하감옥"),
                        new Description("지하감옥 테마"),
                        "https://i.pinimg.com/236x.jpg"
                )
        );
        Theme theme5 = themeRepository.findById(theme5Id).get();

        Long theme6Id = themeRepository.save(
                new Theme(
                        new ThemeName("놀이공원"),
                        new Description("놀이공원 테마"),
                        "https://i.pinimg.com/236x.jpg"
                )
        );
        Theme theme6 = themeRepository.findById(theme6Id).get();

        Long theme7Id = themeRepository.save(
                new Theme(
                        new ThemeName("신전"),
                        new Description("신전 테마"),
                        "https://i.pinimg.com/236x.jpg"
                )
        );
        Theme theme7 = themeRepository.findById(theme7Id).get();

        Long theme8Id = themeRepository.save(
                new Theme(
                        new ThemeName("우주 정거장"),
                        new Description("우주 정거장 테마"),
                        "https://i.pinimg.com/236x.jpg"
                )
        );
        Theme theme8 = themeRepository.findById(theme8Id).get();

        Long theme9Id = themeRepository.save(
                new Theme(
                        new ThemeName("우테코 미션1"),
                        new Description("우테코 미션1 테마"),
                        "https://i.pinimg.com/236x.jpg"
                )
        );
        Theme theme9 = themeRepository.findById(theme9Id).get();

        Long theme10Id = themeRepository.save(
                new Theme(
                        new ThemeName("우테코 미션2"),
                        new Description("우테코 미션2 테마"),
                        "https://i.pinimg.com/236x.jpg"
                )
        );
        Theme theme10 = themeRepository.findById(theme10Id).get();

        reservationRepository.save(new Reservation(new ReservationName("호기"), LocalDate.now(), theme1, reservationTime));
        reservationRepository.save(new Reservation(new ReservationName("코코"), LocalDate.now(), theme1, reservationTime));
        reservationRepository.save(new Reservation(new ReservationName("카키"), LocalDate.now(), theme2, reservationTime));
        reservationRepository.save(new Reservation(new ReservationName("네오"), LocalDate.now(), theme2, reservationTime));
        reservationRepository.save(new Reservation(new ReservationName("로로"), LocalDate.now(), theme2, reservationTime));
        reservationRepository.save(new Reservation(new ReservationName("솔라"), LocalDate.now(), theme3, reservationTime));
        reservationRepository.save(new Reservation(new ReservationName("브리"), LocalDate.now(), theme4, reservationTime));
        reservationRepository.save(new Reservation(new ReservationName("토미"), LocalDate.now(), theme5, reservationTime));
        reservationRepository.save(new Reservation(new ReservationName("포비"), LocalDate.now(), theme6, reservationTime));
        reservationRepository.save(new Reservation(new ReservationName("리사"), LocalDate.now(), theme7, reservationTime));
        reservationRepository.save(new Reservation(new ReservationName("브라운"), LocalDate.now(), theme8, reservationTime));
        reservationRepository.save(new Reservation(new ReservationName("러너덕"), LocalDate.now(), theme9, reservationTime));
        reservationRepository.save(new Reservation(new ReservationName("제임스"), LocalDate.now(), theme10, reservationTime));

        List<Theme> themes = themeRepository.findTopTenThemesDescendingOfLastWeek();

        assertAll(
                () -> assertThat(themes.get(0).getName()).isEqualTo("액션"),
                () -> assertThat(themes.size()).isEqualTo(10)
        );
    }

    @Test
    @DisplayName("id를 받아 삭제한다.")
    void deleteTest() {
        Theme theme = new Theme(
                new ThemeName("공포"),
                new Description("무서운 테마"),
                "https://i.pinimg.com/236x.jpg"
        );
        Long themeId = themeRepository.save(theme);
        themeRepository.delete(themeId);
        List<Theme> themes = themeRepository.findAll();

        assertThat(themes.size()).isEqualTo(0);
    }
}
