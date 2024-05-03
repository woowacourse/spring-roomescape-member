package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.dto.request.CreateThemeRequest;
import roomescape.theme.dto.response.CreateThemeResponse;
import roomescape.theme.dto.response.FindPopularThemesResponse;
import roomescape.theme.dto.response.FindThemeResponse;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.util.DummyDataFixture;

@SpringBootTest
class ThemeServiceTest extends DummyDataFixture {

    @InjectMocks
    private ThemeService themeService;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("예약 테마 생성 시 해당 데이터의 id값을 반환한다.")
    void createReservationTime() {
        // given
        CreateThemeRequest createThemeRequest = new CreateThemeRequest(
                "마크", "노력함", "https://asd.com");

        // stub
        Mockito.when(themeRepository.save(any(Theme.class))).thenReturn(getThemeById(1L));

        // when & then
        assertThat(themeService.createTheme(createThemeRequest))
                .isEqualTo(CreateThemeResponse.of(getThemeById(1L)));
    }

    @Test
    @DisplayName("예약 테마 목록 조회 시 저장된 예약 테마에 대한 정보를 반환한다.")
    void getThemes() {
        // stub
        Mockito.when(themeRepository.findAll())
                .thenReturn(super.getPreparedThemes());

        // when & then
        assertThat(themeService.getThemes()).containsExactly(
                new FindThemeResponse(1L, "공포", "무서워", "https://zerolotteworld.com/storage/WAYH10LvyaCuAb9ndj1apZIpzEAdpjeAhPR7Gb7J.jpg"),
                new FindThemeResponse(2L, "액션", "신나", "https://sherlock-holmes.co.kr/attach/theme/17000394031.jpg"),
                new FindThemeResponse(3L, "SF", "신기해", "https://sherlock-holmes.co.kr/attach/theme/16941579841.jpg"),
                new FindThemeResponse(4L, "로맨스", "달달해", "https://i.postimg.cc/vDFKqct1/theme.jpg"),
                new FindThemeResponse(5L, "코미디", "웃기다", "https://sherlock-holmes.co.kr/attach/theme/16956118601.jpg"),
                new FindThemeResponse(6L, "드라마", "반전", "https://sherlock-holmes.co.kr/attach/theme/16941579841.jpg"),
                new FindThemeResponse(7L, "잠입", "스릴있어",
                        "https://search.pstatic.net/sunny/?src=https%3A%2F%2Ffile.miricanvas.com%2Ftemplate_thumb%2F2022%2F05%2F15%2F13%2F50%2Fk2nje40j0jwztqza%2Fthumb.jpg&type=sc960_832"),
                new FindThemeResponse(8L, "오락", "재밌어",
                        "http://jamsil.cubeescape.co.kr/theme/basic_room2/img/rain/room15.jpg"),
                new FindThemeResponse(9L, "판타지", "말이 안돼", "https://i.postimg.cc/8k2PQ4yv/theme.jpg"),
                new FindThemeResponse(10L, "감성", "감동적", "https://sherlock-holmes.co.kr/attach/theme/16788523411.jpg"));
    }

    @Test
    @DisplayName("예약이 많은 상위 10개의 테마에 대한 정보를 반환한다.")
    void getPopularThemes() {
        // stub
        Mockito.when(themeRepository.findOrderByReservation())
                .thenReturn(super.getPreparedThemes());

        // when & then
        assertThat(themeService.getPopularThemes()).containsExactly(
                new FindPopularThemesResponse(1L, "공포", "무서워", "https://zerolotteworld.com/storage/WAYH10LvyaCuAb9ndj1apZIpzEAdpjeAhPR7Gb7J.jpg"),
                new FindPopularThemesResponse(2L, "액션", "신나", "https://sherlock-holmes.co.kr/attach/theme/17000394031.jpg"),
                new FindPopularThemesResponse(3L, "SF", "신기해", "https://sherlock-holmes.co.kr/attach/theme/16941579841.jpg"),
                new FindPopularThemesResponse(4L, "로맨스", "달달해", "https://i.postimg.cc/vDFKqct1/theme.jpg"),
                new FindPopularThemesResponse(5L, "코미디", "웃기다", "https://sherlock-holmes.co.kr/attach/theme/16956118601.jpg"),
                new FindPopularThemesResponse(6L, "드라마", "반전", "https://sherlock-holmes.co.kr/attach/theme/16941579841.jpg"),
                new FindPopularThemesResponse(7L, "잠입", "스릴있어",
                        "https://search.pstatic.net/sunny/?src=https%3A%2F%2Ffile.miricanvas.com%2Ftemplate_thumb%2F2022%2F05%2F15%2F13%2F50%2Fk2nje40j0jwztqza%2Fthumb.jpg&type=sc960_832"),
                new FindPopularThemesResponse(8L, "오락", "재밌어",
                        "http://jamsil.cubeescape.co.kr/theme/basic_room2/img/rain/room15.jpg"),
                new FindPopularThemesResponse(9L, "판타지", "말이 안돼", "https://i.postimg.cc/8k2PQ4yv/theme.jpg"),
                new FindPopularThemesResponse(10L, "감성", "감동적", "https://sherlock-holmes.co.kr/attach/theme/16788523411.jpg"));
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 테마를 삭제한다.")
    void deleteById() {
        // given
        long timeId = 10L;

        // stub
        Mockito.when(themeRepository.existsById(timeId)).thenReturn(true);
        Mockito.when(reservationRepository.existsByThemeId(timeId)).thenReturn(false);

        // when
        themeService.deleteById(timeId);

        // then
        Mockito.verify(themeRepository, Mockito.times(1)).deleteById(timeId);
    }

    @Test
    @DisplayName("해당하는 id와 동일한 저장된 예약 시간이 없는 경우 예외가 발생한다.")
    void deleteById_ifNotExist_throwException() {
        // given
        long timeId = 10L;

        // stub
        Mockito.when(themeRepository.findById(timeId))
                .thenReturn(Optional.empty());

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

        // stub
        Mockito.when(themeRepository.existsById(themedId)).thenReturn(true);
        Mockito.when(reservationRepository.existsByThemeId(themedId)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> themeService.deleteById(themedId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("테마를 사용 중인 예약이 존재합니다.");
    }
}
