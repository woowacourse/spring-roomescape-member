package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Description;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.ThemeName;
import roomescape.reservation.dto.ThemeSaveRequest;
import roomescape.reservation.repository.ThemeRepository;

@SpringBootTest
@Transactional
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Test
    @DisplayName("중복된 테마 이름을 추가할 수 없다.")
    void duplicateThemeNameExceptionTest() {
        ThemeSaveRequest theme1 = new ThemeSaveRequest("공포", "무서운 테마", "https://ab.com/1x.png");
        themeService.save(theme1);

        ThemeSaveRequest theme2 = new ThemeSaveRequest("공포", "무서움", "https://cd.com/2x.jpg");
        assertThatThrownBy(() -> themeService.save(theme2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테마 아이디로 조회 시 존재하지 않는 아이디면 예외가 발생한다.")
    void findByIdExceptionTest() {
        assertThatThrownBy(() -> themeService.findById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
