package integration.theme;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import integration.BaseIntegrationTest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import roomescape.domain.Theme;
import roomescape.repository.ThemeRepository;

class ThemeRepositoryTest extends BaseIntegrationTest {
    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ThemeDataSource themeDataSource;

    @BeforeEach
    void setUp() {
        themeDataSource.clearId();
        themeDataSource.clearTable();
    }

    @Test
    void 테마를_저장하고_ID로_조회할_수_있다() {
        // given
        Theme theme = new Theme("바니의 집", "바니의 집입니다", "http://image.png/image.com");

        // when
        Theme saved = themeRepository.save(theme);

        // then
        assertThat(themeRepository.findById(saved.getId())).isPresent();
    }

    @Test
    void 활성화된_테마_조회가_있는지_확인() {
        // given
        Theme theme = new Theme("바니의 집", "바니의 집입니다", "http://image.png/image.com");
        themeRepository.save(theme);

        // when
        boolean result = themeRepository.isActiveByName("바니의 집");

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 활성화된_테마가_있을_때_같은_테마를_추가하면_제약_위반() {
        // given
        Theme theme = new Theme("바니의 집", "바니의 집입니다", "http://image.png/image.com");
        themeRepository.save(theme);

        // when & then
        assertThatThrownBy(() -> themeRepository.save(theme))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 비성활화된_같은_테마는_여러개_존재_가능() {
        // given
        Theme first = new Theme(1L, "바니의 집", "바니의 집입니다", "http://image.png/image.com", false);
        Theme second = new Theme(1L, "바니의 집", "바니의 집입니다", "http://image.png/image.com", false);

        // when & then
        assertThatCode(() -> {
            themeRepository.save(first);
            themeRepository.save(second);
        }).doesNotThrowAnyException();
    }

    @Test
    void 테마_정보가_수정이_된다() {
        // given
        Theme theme = new Theme("바니의 집", "바니의 집입니다", "http://image.png/image.com");
        Theme savedTheme = themeRepository.save(theme);
        savedTheme.deactivate();

        // when
        themeRepository.update(savedTheme);

        // then
        Optional<Theme> found = themeRepository.findById(savedTheme.getId());
        assertThat(found).isPresent();
        assertThat(found.get().isActive()).isFalse();
    }
}
