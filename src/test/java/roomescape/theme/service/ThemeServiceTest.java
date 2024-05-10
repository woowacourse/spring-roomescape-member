package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.repository.FakeReservationRepository;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.dto.request.CreateThemeRequest;
import roomescape.theme.dto.response.CreateThemeResponse;
import roomescape.theme.dto.response.FindPopularThemesResponse;
import roomescape.theme.dto.response.FindThemeResponse;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.FakeThemeRepository;
import roomescape.theme.repository.ThemeRepository;
import roomescape.util.ReservationFixture;
import roomescape.util.ThemeFixture;

class ThemeServiceTest {
    private ThemeService themeService;

    private ThemeRepository themeRepository;
    private ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        themeRepository = new FakeThemeRepository(reservationRepository);

        themeService = new ThemeService(themeRepository, reservationRepository);
    }

    @Test
    @DisplayName("예약 테마 생성 시 해당 데이터의 id값을 반환한다.")
    void createReservationTime() {
        // given
        CreateThemeRequest createThemeRequest = new CreateThemeRequest("몰리의 신기한 놀이", "재미있는 테마임", "https://asdfg.ccc");

        // when
        CreateThemeResponse createThemeResponse = themeService.createTheme(createThemeRequest);

        // then
        assertThat(createThemeResponse.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("예약 테마 목록 조회 시 저장된 예약 테마에 대한 정보를 반환한다.")
    void getThemes() {
        // given
        List<Theme> themes = ThemeFixture.get(2);
        Theme saveTheme1 = themeRepository.save(themes.get(0));
        Theme saveTheme2 = themeRepository.save(themes.get(1));

        // when & then
        assertThat(themeService.getThemes()).containsExactly(
                FindThemeResponse.from(saveTheme1),
                FindThemeResponse.from(saveTheme2)
        );
    }

    @Test
    @DisplayName("예약이 많은 상위 10개의 테마에 대한 정보를 반환한다.")
    void getPopularThemes() {
        // given
        List<Theme> savedThemes = ThemeFixture.get(10).stream()
                .map(theme -> themeRepository.save(theme))
                .toList();

        reservationRepository.save(ReservationFixture.getOneWithTheme(savedThemes.get(7)));
        reservationRepository.save(ReservationFixture.getOneWithTheme(savedThemes.get(7)));
        reservationRepository.save(ReservationFixture.getOneWithTheme(savedThemes.get(5)));

        // when & then
        assertThat(themeService.getPopularThemes(10)).containsExactly(
                FindPopularThemesResponse.from(savedThemes.get(7)),
                FindPopularThemesResponse.from(savedThemes.get(7)),
                FindPopularThemesResponse.from(savedThemes.get(5))
        );
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 테마를 삭제한다.")
    void deleteById() {
        // given
        Theme savedTheme = themeRepository.save(ThemeFixture.getOne());

        // when
        themeService.deleteById(savedTheme.getId());

        // then
        assertThat(themeRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간이 없는 경우 예외가 발생한다.")
    void deleteById_ifNotExist_throwException() {
        // given
        Long timeId = 1L;

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(timeId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("삭제하려는 테마가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("해당하는 시간을 사용 중인 예약이 존재할 경우 예외가 발생한다.")
    void deleteById_ifAlreadyUsed_throwException() {
        // given
        Theme savedTheme = themeRepository.save(ThemeFixture.getOne());
        reservationRepository.save(ReservationFixture.getOneWithTheme(savedTheme));

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(savedTheme.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("삭제하려는 테마를 사용 중인 예약이 존재합니다.");
    }
}
