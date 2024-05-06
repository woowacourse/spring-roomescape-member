package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.fixture.Fixture;
import roomescape.reservation.fake.FakeReservationRepository;
import roomescape.theme.dto.request.CreateThemeRequest;
import roomescape.theme.dto.response.CreateThemeResponse;
import roomescape.theme.dto.response.FindPopularThemesResponse;
import roomescape.theme.dto.response.FindThemeResponse;
import roomescape.theme.fake.FakeThemeRepository;

class ThemeServiceTest {

    private static ThemeService themeService;
    private static FakeReservationRepository reservationRepository;
    private static FakeThemeRepository themeRepository;

    @BeforeAll
    static void beforeAll() {
        reservationRepository = new FakeReservationRepository();
        themeRepository = new FakeThemeRepository(reservationRepository);
        themeService = new ThemeService(themeRepository, reservationRepository);
    }

    @BeforeEach
    void setUp() {
        reservationRepository.clear();
        themeRepository.clear();

        reservationRepository.save(Fixture.RESERVATION_1);
        reservationRepository.save(Fixture.RESERVATION_2);
        reservationRepository.save(Fixture.RESERVATION_3);
        themeRepository.save(Fixture.THEME_1);
        themeRepository.save(Fixture.THEME_2);
        themeRepository.save(Fixture.THEME_3);
    }

    @Test
    @DisplayName("예약 테마 생성 시 해당 데이터를 반환한다.")
    void createReservationTime() {
        // given
        var request = new CreateThemeRequest("마크", "노력함", "https://asd.com");

        // when & then
        CreateThemeResponse theme = themeService.createTheme(request);

        assertAll(
                () -> assertThat(theme.name()).isEqualTo("마크"),
                () -> assertThat(theme.description()).isEqualTo("노력함"),
                () -> assertThat(theme.thumbnail()).isEqualTo("https://asd.com"));
    }

    @Test
    @DisplayName("예약 테마 목록 조회 시 저장된 예약 테마에 대한 정보를 반환한다.")
    void getThemes() {
        // when & then
        assertThat(themeService.getThemes())
                .containsExactly(
                        FindThemeResponse.of(Fixture.THEME_1),
                        FindThemeResponse.of(Fixture.THEME_2),
                        FindThemeResponse.of(Fixture.THEME_3));
    }

    @Test
    @DisplayName("예약이 많은 상위 10개의 테마에 대한 정보를 반환한다.")
    void getPopularThemes() {
        // when & then
        assertThat(themeService.getPopularThemes())
                .containsExactlyInAnyOrderElementsOf(List.of(
                        FindPopularThemesResponse.of(Fixture.THEME_2),
                        FindPopularThemesResponse.of(Fixture.THEME_1)));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 테마를 삭제한다.")
    void deleteById() {
        // when & then
        assertThatCode(() -> themeService.deleteById(3L)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간이 없는 경우 예외가 발생한다.")
    void deleteById_ifNotExist_throwException() {
        // given
        long timeId = 0L;

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(timeId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당하는 테마가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("해당하는 시간을 사용 중인 예약이 존재할 경우 예외가 발생한다.")
    void deleteById_ifAlreadyUsed_throwException() {
        // given
        long themedId = 2L;

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(themedId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("테마를 사용 중인 예약이 존재합니다.");
    }
}
