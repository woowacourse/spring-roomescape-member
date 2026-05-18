package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.dto.theme.AddThemeRequest;
import roomescape.exception.exception.DataReferencedException;
import roomescape.exception.exception.DuplicatedResourceException;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.theme.ThemeRepository;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static roomescape.exception.dto.ErrorCode.*;

@ExtendWith(MockitoExtension.class)
public class ThemeServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private ThemeService themeService;

    @Test
    @DisplayName("동일한 이름의 테마가 존재하는 경우 테마 추가 시 예외 테스트")
    void addThemeFailedWhenDuplicatedTest() {
        when(themeRepository.existsByName(any())).thenReturn(true);

        assertThatThrownBy(() -> themeService.addTheme(
                new AddThemeRequest("테마1", "테마 설명", "image url")
        ))
                .isExactlyInstanceOf(DuplicatedResourceException.class)
                .hasMessage(DUPLICATED_THEME.getMessage());
    }

    @Test
    @DisplayName("정상 삭제 테스트")
    void deleteThemeTest() {
        when(reservationRepository.existsByThemeId(anyLong())).thenReturn(false);

        assertThatCode(() -> themeService.deleteTheme(1))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("외부 사용이 되었을 때 삭제 시 예외 테스트")
    void deleteFailedWhenInUseTest() {
        when(reservationRepository.existsByThemeId(anyLong())).thenReturn(true);

        assertThatThrownBy(() -> themeService.deleteTheme(1))
                .isExactlyInstanceOf(DataReferencedException.class)
                .hasMessage(CANNOT_DELETE_THEME_IN_USE.getMessage());
    }

    @Test
    @DisplayName("조회 시점엔 없었으나 삭제 시점에 제약조건 위반이 발생한 경우 예외 테스트")
    void deleteFailedByIntegrityTest() {
        when(reservationRepository.existsByThemeId(anyLong())).thenReturn(false);
        doThrow(new DataIntegrityViolationException("정합성 오류"))
                .when(themeRepository).deleteTheme(anyLong());

        assertThatThrownBy(() -> themeService.deleteTheme(1))
                .isExactlyInstanceOf(DataReferencedException.class)
                .hasMessage(INTEGRITY_VIOLATION_ON_DELETE.getMessage());
    }
}
