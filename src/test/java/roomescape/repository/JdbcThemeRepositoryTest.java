package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class JdbcThemeRepositoryTest {

    @Autowired
    ThemeRepository themeRespository;

    @DisplayName("테마를 저장한다")
    @Test
    void 테마를_저장하면_id를_부여한다() {
        // given
        Theme theme = Theme.create("귀신찾기", "귀신을 찾는다", "example.com");

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
                Theme.create("귀신찾기", "귀신을 찾는다", "example.com")
        );

        // when
        Optional<Theme> result = themeRespository.findById(saved.getId());

        // then
        assertThat(result)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @DisplayName("저장된 모든 테마를 조회한다")
    @Test
    void 저장된_모든_테마를_조회한다() {
        // given
        Theme savedHorror = themeRespository.save(
                Theme.create("귀신찾기", "귀신을 찾는다", "example.com")
        );
        Theme savedSuspect = themeRespository.save(
                Theme.create("추리", "추리한다", "example.com")
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

    @DisplayName("인기 테마의 id를 조회한다")
    @Test
    @Sql("/data.sql")
    void 최근_1주_동안의_예약_상위_10개의_테마를_조회한다() {
        List<Long> popularThemes = themeRespository.findPopularThemeIds();
        assertThat(popularThemes)
                .containsExactly(
                        1L, 2L, 3L, // 1순위: 테마의 예약 수 내림차순 정렬
                        6L, 5L, 4L, 8L, 7L, // 2순위: 예약 수가 같으면 테마 이름 오름차순 정렬
                        10L, 9L // 예약 개수가 0개여도, 상위 10위 이내라면 조회되어야 함 (예약 개수 0개인 테마들은 2순위 정렬 기준으로 비교)
                );
    }

    @DisplayName("id에 해당하는 테마를 삭제한다")
    @Test
    void 테마를_삭제한다() {
        // given
        Theme savedHorror = themeRespository.save(
                Theme.create("귀신찾기", "귀신을 찾는다", "example.com")
        );

        // when
        themeRespository.delete(savedHorror.getId());
        Optional<Theme> result = themeRespository.findById(savedHorror.getId());

        // then
        assertThat(result).isNotPresent();
    }
}
