package roomescape.theme.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.exception.DuplicateException;
import roomescape.exception.ResourceInUseException;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminThemeServiceTest {

    @Mock
    private ThemeRepository themeRepository;

    @InjectMocks
    private AdminThemeService adminThemeService;

    @Test
    void 테마를_추가할_수_있다() {
        Theme saved = new Theme(1L, "Theme A", "desc", "thumb");

        when(themeRepository.save(eq("Theme A"), eq("desc"), eq("thumb"))).thenReturn(saved);

        Theme result = adminThemeService.save("Theme A", "desc", "thumb");

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Theme A");
    }

    @Test
    void 테마가_중복되면_예외가_발생한다() {
        when(themeRepository.save(eq("Theme A"), eq("desc"), eq("thumb")))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        assertThatThrownBy(() -> adminThemeService.save("Theme A", "desc", "thumb"))
                .isInstanceOf(DuplicateException.class)
                .extracting(Throwable::getMessage)
                .isEqualTo("같은 이름의 테마가 존재합니다.");
    }

    @Test
    void 예약이_있으면_테마를_삭제할_수_없다() {
        when(themeRepository.countByThemeId(eq(1L))).thenReturn(1);

        assertThatThrownBy(() -> adminThemeService.delete(1L))
                .isInstanceOf(ResourceInUseException.class)
                .extracting(Throwable::getMessage)
                .isEqualTo("예약이 있어 삭제할 수 없습니다");
    }

    @Test
    void 예약이_없으면_테마를_삭제할_수_있다() {
        when(themeRepository.countByThemeId(eq(1L))).thenReturn(0);

        assertThatCode(() -> adminThemeService.delete(1L))
                .doesNotThrowAnyException();
    }
}
