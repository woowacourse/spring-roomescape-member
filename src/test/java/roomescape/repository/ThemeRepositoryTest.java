package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Theme;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Import(H2ThemeRepository.class)
@JdbcTest
class ThemeRepositoryTest {

    final List<Theme> sampleThemes = List.of(
            new Theme(null, "Theme 1", "Description 1", "Thumbnail 1"),
            new Theme(null, "Theme 2", "Description 2", "Thumbnail 2"),
            new Theme(null, "Theme 3", "Description 3", "Thumbnail 3"),
            new Theme(null, "Theme 4", "Description 4", "Thumbnail 4")
    );

    @Autowired
    ThemeRepository themeRepository;

    @Test
    @DisplayName("모든 테마 목록을 조회한다.")
    void findAll() {
        // given
        sampleThemes.forEach(themeRepository::save);

        // when
        final List<Theme> actual = themeRepository.findAll();
        final List<Theme> expected = IntStream.range(0, sampleThemes.size())
                .mapToObj(i -> sampleThemes.get(i).assignId(actual.get(i).getId()))
                .toList();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("특정 id를 통해 테마를 조회한다.")
    void findByIdPresent() {
        // given
        final Theme theme = sampleThemes.get(0);
        final Theme savedTheme = themeRepository.save(theme);
        final Long saveId = savedTheme.getId();

        // when
        final Optional<Theme> actual = themeRepository.findById(saveId);
        final Theme expected = theme.assignId(saveId);

        // then
        assertThat(actual).hasValue(expected);
    }

    @Test
    @DisplayName("존재하지 않는 테마를 조회할 경우 빈 값을 반환한다.")
    void findByIdNotPresent() {
        // given
        final long notExistId = 1L;

        // when
        final Optional<Theme> actual = themeRepository.findById(notExistId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("테마를 저장한다.")
    void save() {
        // given
        final Theme theme = sampleThemes.get(0);

        // when
        final Theme actual = themeRepository.save(theme);
        final Theme expected = theme.assignId(actual.getId());

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("등록된 테마 번호로 삭제한다.")
    void deleteByIdPresent() {
        // given
        final Theme theme = sampleThemes.get(0);
        final Theme savedTheme = themeRepository.save(theme);
        final Long existId = savedTheme.getId();

        // when & then
        assertThat(themeRepository.findById(existId)).isPresent();
        assertThat(themeRepository.delete(existId)).isNotZero();
        assertThat(themeRepository.findById(existId)).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제할 경우 아무런 영향이 없다.")
    void deleteByNotPresent() {
        // given
        final Long notExistId = 1L;

        // when & then
        assertThat(themeRepository.findById(notExistId)).isEmpty();
        assertThat(themeRepository.delete(notExistId)).isZero();
    }
}
