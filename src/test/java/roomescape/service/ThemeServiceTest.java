package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.model.Theme;
import roomescape.repository.ThemeRepository;
import roomescape.repository.dao.ReservationDao;
import roomescape.repository.dao.ThemeDao;
import roomescape.repository.dto.ReservationSavedDto;
import roomescape.service.dto.ThemeDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ThemeServiceTest {

    private ThemeService themeService;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() { // 테케 의존성 분리하기
        ThemeDao themeDao = new FakeThemeDao(new ArrayList<>(List.of(
                new Theme(1, "에버", "공포", "공포.jpg"),
                new Theme(2, "배키", "미스터리", "미스터리.jpg"),
                new Theme(3, "포비", "스릴러", "스릴러.jpg"))));
        ReservationDao reservationDao = new FakeReservationDao(new ArrayList<>(List.of(
                new ReservationSavedDto(1, "브라운", LocalDate.of(2030, 8, 5), 2L, 1L),
                new ReservationSavedDto(1, "리사", LocalDate.of(2030, 8, 1), 2L, 2L))));
        themeRepository = new ThemeRepository(reservationDao, themeDao);
        themeService = new ThemeService(themeRepository);
    }

    @DisplayName("테마를 조회한다.")
    @Test
    void should_find_all_themes() {
        assertThat(themeService.findAllThemes()).hasSize(3);
    }

    @DisplayName("테마를 저장한다.")
    @Test
    void should_add_theme() {
        ThemeDto themeDto = new ThemeDto("에버", "공포", "공포.jpg");
        themeService.saveTheme(themeDto);
        assertThat(themeService.findAllThemes()).hasSize(4);
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void should_delete_theme() {
        themeService.deleteTheme(1L);
        assertThat(themeService.findAllThemes()).hasSize(2);
    }

    @DisplayName("최근 일주일 간 가장 인기 있는 테마 10개를 조회한다.")
    @Test
    void should_find_popular_theme_of_week() {
        List<Theme> popularThemes = themeService.findPopularThemes();
        // TODO: now 라서 인기 테마 존재하지 않음
        assertThat(popularThemes).hasSize(0);
    }
}