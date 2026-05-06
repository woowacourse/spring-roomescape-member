package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;

@SpringBootTest
@Transactional
class JdbcThemeRepositoryTest {

    @Autowired
    ThemeRespository themeRespository;

    @DisplayName("테마를 저장한다")
    @Test
    void 테마를_저장하면_id를_부여한다() {
        // given
        Theme theme = new Theme("귀신찾기", "귀신을 찾는다", "example.com");

        // when
        Theme saved = themeRespository.save(theme);

        // then
        assertThat(saved.getId()).isNotNull();
    }

    @DisplayName("테마를 id로 조회한다")
    @Test
    void 테마를_id로_조회한다() {
        // given
        Theme saved = themeRespository.save(
                new Theme("귀신찾기", "귀신을 찾는다", "example.com")
        );

        // when
        Optional<Theme> result = themeRespository.findById(saved.getId());

        // then
        assertThat(result.get()).isEqualTo(saved);
    }

    @DisplayName("저장된 모든 테마를 조회한다")
    @Test
    void 저장된_모든_테마를_조회한다() {
        // given
        Theme savedHorror = themeRespository.save(
                new Theme("귀신찾기", "귀신을 찾는다", "example.com")
        );
        Theme savedSuspect = themeRespository.save(
                new Theme("추리", "추리한다", "example.com")
        );

        // when
        List<Theme> foundThemes = themeRespository.findAll();

        // then
        assertThat(foundThemes)
                .hasSize(2)
                .containsExactlyInAnyOrder(
                        savedHorror,
                        savedSuspect
                );
    }

    @DisplayName("id에 해당하는 테마를 삭제한다")
    @Test
    void 테마를_삭제한다() {
        // given
        Theme savedHorror = themeRespository.save(
                new Theme("귀신찾기", "귀신을 찾는다", "example.com")
        );

        // when
        themeRespository.delete(savedHorror.getId());
        Optional<Theme> result = themeRespository.findById(savedHorror.getId());

        // then
        assertThat(result).isNotPresent();
    }
}
