package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@Sql(value = "/schema.sql", executionPhase = BEFORE_TEST_METHOD)
class ThemeRepositoryTest {

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("모든 테마 정보를 조회한다.")
    @Test
    void find() {
        // When
        final List<Theme> themes = themeRepository.findAll();

        // Then
        assertThat(themes).hasSize(2);
    }

    @DisplayName("테마 정보를 저장한다.")
    @Test
    void saveTest() {
        // Given
        final Theme theme = Theme.of(
                "테바의 비밀친구",
                "테바의 은밀한 비밀친구",
                "대충 테바 사진 링크");

        // When
        final Theme savedTheme = themeRepository.save(theme);

        // Then
        final List<Theme> themes = themeRepository.findAll();
        assertAll(
                () -> assertThat(themes).hasSize(3),
                () -> assertThat(savedTheme.getId()).isEqualTo(3L),
                () -> assertThat(savedTheme.getName().getValue()).isEqualTo(theme.getName().getValue()),
                () -> assertThat(savedTheme.getDescription().getValue()).isEqualTo(theme.getDescription().getValue()),
                () -> assertThat(savedTheme.getThumbnail()).isEqualTo(theme.getThumbnail())
        );
    }

    @DisplayName("테마 정보를 삭제한다.")
    @Test
    void deleteByIdTest() {
        // When
        themeRepository.deleteById(2L);

        // Then
        final List<Theme> themes = themeRepository.findAll();
        assertThat(themes).hasSize(1);
    }
}
