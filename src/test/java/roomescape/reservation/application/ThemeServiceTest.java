package roomescape.reservation.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.infra.ScheduleRepository;
import roomescape.reservation.infra.ThemeRepository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Transactional
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ThemeServiceTest {
    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ThemeService themeService;

    @Test
    @DisplayName("스케줄에 테마에 대한 참조가 존재하면 테마 삭제에 실패한다.")
    void delete_실패_테스트_1() {
        // given
        long themeId = 1L;
        when(scheduleRepository.existsByThemeId(themeId)).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> themeService.delete(themeId))
                .isInstanceOf(IllegalStateException.class);

        verify(themeRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("스케줄에 테마에 대한 참조가 존재하지 않으면 테마 삭제에 성공한다.")
    void delete_성공_테스트() {
        // given
        long themeId = 1L;
        when(scheduleRepository.existsByThemeId(themeId)).thenReturn(false);

        // when
        themeService.delete(themeId);

        // then
        verify(themeRepository).deleteById(anyLong());
    }

}
