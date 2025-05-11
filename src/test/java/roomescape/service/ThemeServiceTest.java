package roomescape.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.common.exception.DuplicatedException;
import roomescape.common.exception.ResourceInUseException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ThemeDao;
import roomescape.controller.theme.dto.ThemeRequestDto;
import roomescape.controller.theme.dto.ThemeResponseDto;
import roomescape.model.Theme;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    private ThemeDao themeDao;

    @Mock
    private ReservationDao reservationDao;

    @InjectMocks
    private ThemeService themeService;

    @DisplayName("모든 테마를 조회")
    @Test
    void test() {
        // given
        Theme theme1 = new Theme(1L, "theme1", "desc1", "thumb1");
        Theme theme2 = new Theme(2L, "theme2", "desc2", "thumb2");

        when(themeDao.findAll()).thenReturn(List.of(theme1, theme2));

        // when
        List<ThemeResponseDto> themes = themeService.findAllThemes();

        // then
        assertThat(themes).hasSize(2);
        assertThat(themes).extracting("name").containsExactlyInAnyOrder("theme1", "theme2");
    }

    @DisplayName("테마를 저장")
    @Test
    void test1() {
        // given
        ThemeRequestDto request = new ThemeRequestDto("Mystery", "Scary room", "thumbnail.jpg");
        when(themeDao.isDuplicatedNameExisted("Mystery")).thenReturn(false);
        when(themeDao.saveTheme(any(Theme.class))).thenReturn(1L);

        // when
        ThemeResponseDto response = themeService.saveTheme(request);

        // then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Mystery");
    }

    @DisplayName("중복된 테마는 저장할 수 없다")
    @Test
    void test2() {
        // given
        ThemeRequestDto request = new ThemeRequestDto("Mystery", "Scary room", "thumbnail.jpg");
        when(themeDao.isDuplicatedNameExisted("Mystery")).thenReturn(true);

        // expect
        assertThatThrownBy(() -> themeService.saveTheme(request))
                .isInstanceOf(DuplicatedException.class)
                .hasMessage("중복된 테마는 등록할 수 없습니다.");
    }

    @DisplayName("예약 정보가 없는 테마는 삭제된다")
    @Test
    void test3() {
        // given
        Long id = 1L;
        when(reservationDao.existsByThemeId(id)).thenReturn(false);
        doNothing().when(themeDao).deleteById(id);

        // when
        themeService.deleteTheme(id);

        // then
        verify(reservationDao).existsByThemeId(id);
        verify(themeDao).deleteById(id);
    }

    @DisplayName("예약 정보가 있는 테마는 삭제할 수 없다")
    @Test
    void test4() {
        // given
        Long id = 1L;
        when(reservationDao.existsByThemeId(id)).thenReturn(true);

        // expect
        assertThatThrownBy(() -> themeService.deleteTheme(id))
                .isInstanceOf(ResourceInUseException.class)
                .hasMessage("삭제하고자 하는 테마에 예약된 정보가 있습니다.");

        // then
        verify(reservationDao).existsByThemeId(id);
        verify(themeDao, never()).deleteById(id);
    }

    @DisplayName("인기 테마를 조회한다")
    @Test
    void test5() {
        // given
        LocalDate today = LocalDate.now();
        Theme popularTheme = new Theme(1L, "Popular", "Desc", "thumb.jpg");

        when(themeDao.findThemesByReservationVolumeBetweenDates(eq(today), anyInt(), anyInt()))
                .thenReturn(List.of(popularTheme));

        // when
        List<ThemeResponseDto> result = themeService.findPopularThemes(today);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Popular");
    }
}