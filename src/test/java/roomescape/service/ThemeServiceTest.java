package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeSaveRequest;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("테마 저장")
    @Test
    void save() {
        // given
        final ThemeSaveRequest themeSaveRequest = new ThemeSaveRequest("감자", "설명", "섬네일");
        // when
        final ThemeResponse themeResponse = themeService.saveTheme(themeSaveRequest);

        // then
        assertThat(themeResponse).isEqualTo(new ThemeResponse(1L, "감자", "설명", "섬네일"));
    }

    @DisplayName("테마 조회")
    @Test
    void getThemes() {
        // given
        final ThemeSaveRequest themeSaveRequest = new ThemeSaveRequest("감자", "설명", "섬네일");
        final ThemeResponse themeResponse = themeService.saveTheme(themeSaveRequest);

        // when
        final List<ThemeResponse> themeResponses = themeService.getThemes();

        // then
        assertThat(themeResponses).hasSize(1)
                .containsExactly(new ThemeResponse(1L, "감자", "설명", "섬네일"));
    }

    @DisplayName("테마 삭제")
    @Test
    void deleteTheme() {
        // given
        final ThemeSaveRequest themeSaveRequest = new ThemeSaveRequest("감자", "설명", "섬네일");
        final ThemeResponse themeResponse = themeService.saveTheme(themeSaveRequest);

        // when
        themeService.deleteTheme(themeResponse.id());

        // then
        assertThat(themeService.getThemes()).hasSize(0);
    }

    @DisplayName("존재하지 않는 테마 삭제")
    @Test
    void deleteNonExistTheme() {
        assertThatThrownBy(() -> themeService.deleteTheme(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약이 있는 테마 삭제")
    @Test
    void deleteExistReservation() {
        // given
        final ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("10:00")));
        final Theme theme = themeRepository.save(new Theme("이름", "설명", "썸네일"));
        reservationRepository.save(new Reservation("감자", LocalDate.parse("2025-05-13"), time, theme));

        // when & then
        assertThatThrownBy(() -> themeService.deleteTheme(theme.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인기 테마 조회")
    @Test
    @Sql(scripts = {"/test.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
    void getPopularTheme() {
        // given & when
        final List<ThemeResponse> popularThemes = themeService.getPopularThemes(LocalDate.parse("2024-04-29"), 2);

        // then
        assertThat(popularThemes).hasSize(2)
                .containsExactly(new ThemeResponse(1L, "이름1", "설명1", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                        new ThemeResponse(2L, "이름2", "설명2", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
    }
}
