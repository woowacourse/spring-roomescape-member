package roomescape.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.application.dto.ThemeRequest;
import roomescape.application.dto.ThemeResponse;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @InjectMocks
    private ThemeService themeService;

    @Mock
    private ThemeDao themeDao;

    @Mock
    private ReservationDao reservationDao;

    @Test
    @DisplayName("모든 테마를 조회한다")
    void findAllThemes() {
        // given
        List<Theme> themes = List.of(
                new Theme(1L, "테마1", "설명1", "썸네일1"),
                new Theme(2L, "테마2", "설명2", "썸네일2"),
                new Theme(3L, "테마3", "설명3", "썸네일3")
        );

        doReturn(themes).when(themeDao).findAll();

        // when
        List<ThemeResponse> responses = themeService.findAllThemes();

        // then
        assertThat(responses)
                .hasSize(themes.size())
                .extracting("id", "name", "description", "thumbnail")
                .containsExactly(
                        tuple(1L, "테마1", "설명1", "썸네일1"),
                        tuple(2L, "테마2", "설명2", "썸네일2"),
                        tuple(3L, "테마3", "설명3", "썸네일3")
                );

        // verify
        verify(themeDao, times(1)).findAll();
    }

    @Test
    @DisplayName("테마 랭킹을 조회한다")
    void findThemeRank() {
        // given
        List<Long> rankIds = List.of(1L, 2L, 3L);
        List<Theme> themes = List.of(
                new Theme(1L, "테마1", "설명1", "썸네일1"),
                new Theme(2L, "테마2", "설명2", "썸네일2"),
                new Theme(3L, "테마3", "설명3", "썸네일3")
        );

        LocalDate now = LocalDate.now();
        doReturn(rankIds).when(reservationDao)
                .findTop10ByBetweenDates(now.minusDays(8), now.minusDays(1));

        for (int i = 0; i < rankIds.size(); i++) {
            doReturn(themes.get(i)).when(themeDao).findById(rankIds.get(i));
        }

        // when
        List<ThemeResponse> responses = themeService.findThemeRank();

        // then
        assertThat(responses)
                .hasSize(rankIds.size())
                .extracting("id", "name", "description", "thumbnail")
                .containsExactly(
                        tuple(1L, "테마1", "설명1", "썸네일1"),
                        tuple(2L, "테마2", "설명2", "썸네일2"),
                        tuple(3L, "테마3", "설명3", "썸네일3")
                );

        // verify
        verify(reservationDao, times(1)).findTop10ByBetweenDates(any(LocalDate.class), any(LocalDate.class));
        verify(themeDao, times(rankIds.size())).findById(any(Long.class));
    }

    @Test
    @DisplayName("테마를 생성한다")
    void createTheme() {
        // given
        ThemeRequest request = new ThemeRequest("테마", "설명", "썸네일");
        doReturn(1L).when(themeDao).save(any(Theme.class));

        // when
        ThemeResponse response = themeService.createTheme(request);

        // then
        assertThat(response)
                .extracting("id", "name", "description", "thumbnail")
                .containsExactly(1L, "테마", "설명", "썸네일");

        // verify
        verify(themeDao, times(1)).save(any(Theme.class));
    }

    @Test
    @DisplayName("테마를 삭제한다")
    void deleteTheme() {
        // given
        long deleteThemeId = 1L;
        doReturn(false).when(reservationDao).existsByThemeId(deleteThemeId);

        // when
        themeService.deleteTheme(deleteThemeId);

        // verify
        verify(themeDao, times(1)).deleteById(deleteThemeId);
        verify(reservationDao, times(1)).existsByThemeId(deleteThemeId);
    }

    @Test
    @DisplayName("예약이 존재하는 경우 테마를 삭제할 수 없다")
    void deleteReservationTimeFailsWhenReservationExist() {
        // given
        long deleteThemeId = 1L;
        doReturn(true).when(reservationDao).existsByThemeId(deleteThemeId);

        // when && then
        assertThatThrownBy(() -> themeService.deleteTheme(deleteThemeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테마에 존재하는 예약 정보가 있습니다.");

        // verify
        verify(reservationDao, times(1)).existsByThemeId(deleteThemeId);
    }
}
