package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.util.TestDataInitializer;

@SpringBootTest(properties = "spring.datasource.url=jdbc:h2:mem:theme-repository-test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeRepositoryTest {

    private static final LocalDate TODAY = LocalDate.of(2026, 5, 15);
    private static final List<String> USER_NAMES = List.of("사용자일", "사용자이", "사용자삼");

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private TestDataInitializer dataInitializer;

    private ReservationTime ten;
    private ReservationTime eleven;
    private ReservationTime noon;

    @BeforeEach
    void setUp() {
        ten = dataInitializer.createReservationTime(LocalTime.of(10, 0));
        eleven = dataInitializer.createReservationTime(LocalTime.of(11, 0));
        noon = dataInitializer.createReservationTime(LocalTime.of(12, 0));
    }

    @Test
    void 최근_예약_건수를_기준으로_인기_테마를_조회한다() {
        Theme popularTheme = createTheme("인기 테마");
        Theme lessPopularTheme = createTheme("덜 인기 테마");

        createReservations(popularTheme, TODAY.minusDays(1), ten, eleven);
        createReservations(lessPopularTheme, TODAY.minusDays(1), noon);

        List<Theme> themes = themeRepository.findThemesOrderByReservationCount(
                TODAY.minusDays(7),
                TODAY.minusDays(1),
                10
        );

        assertThat(themes).extracting(Theme::getName)
                .containsExactly("인기 테마", "덜 인기 테마");
    }

    @Test
    void 오늘_예약은_인기_테마_집계에서_제외한다() {
        Theme includedTheme = createTheme("집계 대상 테마");
        Theme excludedTheme = createTheme("오늘 예약 테마");

        createReservations(includedTheme, TODAY.minusDays(1), ten);
        createReservations(excludedTheme, TODAY, eleven, noon);

        List<Theme> themes = themeRepository.findThemesOrderByReservationCount(
                TODAY.minusDays(7),
                TODAY.minusDays(1),
                10
        );

        assertThat(themes).extracting(Theme::getName)
                .containsExactly("집계 대상 테마");
    }

    @Test
    void 조회_기간_이전_예약은_인기_테마_집계에서_제외한다() {
        Theme includedTheme = createTheme("기간 내 테마");
        Theme excludedTheme = createTheme("기간 이전 테마");

        createReservations(includedTheme, TODAY.minusDays(7), ten);
        createReservations(excludedTheme, TODAY.minusDays(8), eleven, noon);

        List<Theme> themes = themeRepository.findThemesOrderByReservationCount(
                TODAY.minusDays(7),
                TODAY.minusDays(1),
                10
        );

        assertThat(themes).extracting(Theme::getName)
                .containsExactly("기간 내 테마");
    }

    @Test
    void 예약_수가_같으면_테마_이름순으로_정렬한다() {
        Theme betaTheme = createTheme("B 테마");
        Theme alphaTheme = createTheme("A 테마");

        createReservations(betaTheme, TODAY.minusDays(1), ten);
        createReservations(alphaTheme, TODAY.minusDays(1), eleven);

        List<Theme> themes = themeRepository.findThemesOrderByReservationCount(
                TODAY.minusDays(7),
                TODAY.minusDays(1),
                10
        );

        assertThat(themes).extracting(Theme::getName)
                .containsExactly("A 테마", "B 테마");
    }

    @Test
    void limit_개수만큼_인기_테마를_조회한다() {
        Theme firstTheme = createTheme("A 테마");
        Theme secondTheme = createTheme("B 테마");
        Theme thirdTheme = createTheme("C 테마");

        createReservations(firstTheme, TODAY.minusDays(1), ten, eleven, noon);
        createReservations(secondTheme, TODAY.minusDays(1), ten, eleven);
        createReservations(thirdTheme, TODAY.minusDays(1), noon);

        List<Theme> themes = themeRepository.findThemesOrderByReservationCount(
                TODAY.minusDays(7),
                TODAY.minusDays(1),
                2
        );

        assertThat(themes).extracting(Theme::getName)
                .containsExactly("A 테마", "B 테마");
    }

    @Test
    void 예약이_없는_테마는_인기_테마_조회_결과에서_제외한다() {
        Theme reservedTheme = createTheme("예약 있는 테마");
        createTheme("예약 없는 테마");

        createReservations(reservedTheme, TODAY.minusDays(1), ten);

        List<Theme> themes = themeRepository.findThemesOrderByReservationCount(
                TODAY.minusDays(7),
                TODAY.minusDays(1),
                10
        );

        assertThat(themes).extracting(Theme::getName)
                .containsExactly("예약 있는 테마");
    }

    private Theme createTheme(String name) {
        return dataInitializer.createTheme(name, "설명", "/images/themes/" + name + ".webp");
    }

    private void createReservations(Theme theme, LocalDate date, ReservationTime... times) {
        for (int index = 0; index < times.length; index++) {
            dataInitializer.createReservation(USER_NAMES.get(index), date, times[index].getId(), theme.getId());
        }
    }
}
