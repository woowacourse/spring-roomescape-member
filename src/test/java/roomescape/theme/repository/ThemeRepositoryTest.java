package roomescape.theme.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.theme.dao.ThemeJdbcDao;
import roomescape.theme.domain.Theme;

@ExtendWith(MockitoExtension.class)
class ThemeRepositoryTest {

    private final Theme theme = new Theme(1L, "포레스트", "공포 테마", "thumbnail");

    @InjectMocks
    private ThemeRepository themeRepository;
    @Mock
    private ThemeJdbcDao themeJdbcDao;

    @Test
    @DisplayName("정상적인 테마 저장 요청을 예외 없이 처리한다.")
    void saveTheme() {
        Mockito.when(themeJdbcDao.save(theme))
                .thenReturn(theme);

        Theme savedTheme = themeRepository.saveTheme(theme);

        Assertions.assertThat(savedTheme.getId()).isEqualTo(1);
    }

}
