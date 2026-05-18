package roomescape.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.dto.ThemeCreateRequest;
import roomescape.domain.theme.dto.ThemeResponse;
import roomescape.exception.BusinessException;
import roomescape.repository.ReservationQueryingDao;
import roomescape.repository.ThemeQueryingDao;
import roomescape.repository.ThemeUpdatingDao;
import roomescape.service.ThemeService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    ThemeQueryingDao themeQueryingDao;

    @Mock
    ThemeUpdatingDao themeUpdatingDao;

    @Mock
    ReservationQueryingDao reservationQueryingDao;

    @InjectMocks
    ThemeService themeService;

    @Test
    @DisplayName("테마를 생성할 수 있다.")
    void 테마_생성_성공() {
        // given
        String name = "인형의 집";
        String description = "공포 테마의 클래식, 밤마다 살아 움직이는 인형들이 가득한 저택을 탈출하세요.";
        String url = "https://example.com/1";
        Long themeId = 1L;
        Theme theme = new Theme(themeId, name, description, url);

        ThemeCreateRequest request = new ThemeCreateRequest(name, description, url);

        when(themeUpdatingDao.insert(request))
                .thenReturn(themeId);

        when(themeQueryingDao.findThemeById(themeId))
                .thenReturn(Optional.of(theme));

        // when
        ThemeResponse themeResponse = themeService.create(request);

        // then
        Assertions.assertEquals(name, themeResponse.getName());
        Assertions.assertEquals(description, themeResponse.getDescription());
        Assertions.assertEquals(url, themeResponse.getUrl());
    }

    @Test
    @DisplayName("테마 목록을 조회할 수 있다.")
    void 테마_목록_조회_성공() {
        // given
        Theme theme = new Theme(1L, "인형의 집", "공포 테마의 클래식, 밤마다 살아 움직이는 인형들이 가득한 저택을 탈출하세요.", "https://example.com/1");

        when(themeQueryingDao.findAllTheme())
                .thenReturn(List.of(theme));

        // when
        List<ThemeResponse> themes = themeService.findAll();

        // then
        Assertions.assertEquals(1, themes.size());
        Assertions.assertEquals(theme.getName(), themes.getFirst().getName());
    }

    @Test
    @DisplayName("테마를 삭제할 수 있다.")
    void 테마_삭제_성공() {
        // given
        Long themeId = 1L;

        when(reservationQueryingDao.existsReservationByThemeId(themeId))
                .thenReturn(false);

        // when
        themeService.delete(themeId);

        // then
        verify(themeUpdatingDao, times(1)).delete(themeId);
    }

    @Test
    @DisplayName("테마를 삭제할 때 예약이 있는 테마인 경우 에러가 발생한다.")
    void 테마_삭제_에러_예약이_있는_테마() {
        // given
        Long themeId = 1L;

        when(reservationQueryingDao.existsReservationByThemeId(themeId))
                .thenReturn(true);

        // when && then
        Assertions.assertThrows(BusinessException.class, () -> themeService.delete(themeId));
        verify(themeUpdatingDao, never()).delete(themeId);
    }
}
