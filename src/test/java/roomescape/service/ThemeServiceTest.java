package roomescape.service;

import org.junit.jupiter.api.Assertions;
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

import java.util.List;

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
    void 테마_생성_성공() {
        // given
        String name = "인형의 집";
        String description = "공포 테마의 클래식, 밤마다 살아 움직이는 인형들이 가득한 저택을 탈출하세요.";
        String url = "https://example.com/1";

        ThemeCreateRequest request = new ThemeCreateRequest(name, description, url);

        // when
        ThemeResponse themeResponse = themeService.create(request);

        // then
        Assertions.assertEquals(name, themeResponse.getName());
        Assertions.assertEquals(description, themeResponse.getDescription());
        Assertions.assertEquals(url, themeResponse.getUrl());
    }

    @Test
    void 테마_목록_조회_성공() {
        // given
        String name = "인형의 집";
        String description = "공포 테마의 클래식, 밤마다 살아 움직이는 인형들이 가득한 저택을 탈출하세요.";
        String url = "https://example.com/1";

        Theme theme = new Theme(1L, name, description, url);

        when(themeQueryingDao.findAllTheme())
                .thenReturn(List.of(theme));

        // when
        List<ThemeResponse> themes = themeService.findAll();

        // then
        Assertions.assertEquals(1, themes.size());
        Assertions.assertEquals(name, themes.getFirst().getName());
        Assertions.assertEquals(description, themes.getFirst().getDescription());
        Assertions.assertEquals(url, themes.getFirst().getUrl());
    }

    @Test
    void 테마_삭제_성공() {
        // given
        Long themeId = 1L;
        when(reservationQueryingDao.existsReservationByThemeId(themeId))
                .thenReturn(false);
        when(themeUpdatingDao.delete(themeId))
                .thenReturn(1);

        // when
        themeService.delete(themeId);

        // then
        verify(themeUpdatingDao, times(1)).delete(themeId);
    }

    @Test
    void 테마_삭제_에러_테마_없음() {
        // given
        Long themeId = 1L;
        when(reservationQueryingDao.existsReservationByThemeId(themeId))
                .thenReturn(false);
        when(themeUpdatingDao.delete(themeId))
                .thenReturn(0);

        // when && then
        Assertions.assertThrows(BusinessException.class, () -> themeService.delete(themeId));
    }

    @Test
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
