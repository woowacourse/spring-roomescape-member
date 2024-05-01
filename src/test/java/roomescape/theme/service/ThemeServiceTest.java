package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.theme.repository.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeService themeService;

    @Test
    @DisplayName("존재하지 않는 아이디일 경우 예외가 발생한다.")
    void emptyIdExceptionTest() {
        Long themeId = 1L;

        doReturn(Optional.empty()).when(themeRepository)
                .findById(themeId);

        assertThatThrownBy(() -> themeService.findById(themeId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
