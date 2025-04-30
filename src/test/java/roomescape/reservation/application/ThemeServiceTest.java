package roomescape.reservation.application;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.application.repository.ReservationRepository;
import roomescape.reservation.application.repository.ThemeRepository;
import roomescape.reservation.application.service.ThemeService;
import roomescape.reservation.infrastructure.fake.FakeReservationDao;
import roomescape.reservation.infrastructure.fake.FakeThemeDao;
import roomescape.reservation.presentation.dto.ThemeRequest;
import roomescape.reservation.presentation.dto.ThemeResponse;

public class ThemeServiceTest {
    private ThemeService themeService;

    @BeforeEach
    void init() {
        ThemeRepository themeRepository = new FakeThemeDao();
        ReservationRepository reservationRepository = new FakeReservationDao();
        themeService = new ThemeService(reservationRepository, themeRepository);
    }

    @Test
    @DisplayName("테마 추가 테스트")
    void createThemeTest(){
        // given
        ThemeRequest themeRequest = new ThemeRequest(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        // when
        ThemeResponse theme = themeService.createTheme(themeRequest);

        // then
        assertThat(theme.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("테마 전체 조회 테스트")
    void getThemesTest(){
        // given
        ThemeRequest themeRequest = new ThemeRequest(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
        themeService.createTheme(themeRequest);

        // when
        List<ThemeResponse> themes = themeService.getThemes();

        // then
        assertThat(themes).hasSize(1);
    }

    @Test
    @DisplayName("테마 삭제 테스트")
    void deleteThemeTest(){
        // given
        ThemeRequest themeRequest = new ThemeRequest(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
        themeService.createTheme(themeRequest);

        // when
        themeService.deleteTheme(1L);

        // then
        assertThat(themeService.getThemes()).hasSize(0);
    }

}
