package roomescape.theme.service;


import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import roomescape.exception.DataExistException;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@JdbcTest
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ThemeRepository themeRepository;


    @Test
    void 테마를_저장한다() {
        // given
        final String name = "우가우가";
        final String description = "우가우가 설명";
        final String thumbnail = "따봉우가.jpg";

        // when & then
        Assertions.assertThatCode(() -> {
            themeService.save(name, description, thumbnail);
        }).doesNotThrowAnyException();
    }

    @Test
    void 테마를_삭제한다() {
        // given
        final String name = "우가우가";
        final String description = "우가우가 설명";
        final String thumbnail = "따봉우가.jpg";
        final Theme theme = new Theme(name, description, thumbnail);
        final Long id = themeRepository.save(theme);

        // when & then
        Assertions.assertThatCode(() -> {
            themeService.deleteById(id);
        }).doesNotThrowAnyException();
    }

    @Test
    void 테마를_조회한다() {
        // given
        final String name = "우가우가";
        final String description = "우가우가 설명";
        final String thumbnail = "따봉우가.jpg";
        final Theme theme = new Theme(name, description, thumbnail);
        final Long id = themeRepository.save(theme);

        // when
        final Theme found = themeService.getById(id);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(found.getName()).isEqualTo(name);
            softly.assertThat(found.getDescription()).isEqualTo(description);
            softly.assertThat(found.getThumbnail()).isEqualTo(thumbnail);
        });
    }

    @Test
    void 테마_전체를_조회한다() {
        // given
        final String name1 = "우가우가";
        final String description1 = "우가우가 설명";
        final String thumbnail1 = "따봉우가.jpg";
        final Theme theme1 = new Theme(name1, description1, thumbnail1);
        themeRepository.save(theme1);

        final String name2 = "우가우가2";
        final String description2 = "우가우가2 설명";
        final String thumbnail2 = "따봉우가2.jpg";
        final Theme theme2 = new Theme(name2, description2, thumbnail2);
        themeRepository.save(theme2);

        // when
        final List<Theme> themes = themeService.findAll();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(themes).hasSize(2);
            softly.assertThat(themes.get(0).getName()).isEqualTo(name1);
            softly.assertThat(themes.get(0).getDescription()).isEqualTo(description1);
            softly.assertThat(themes.get(0).getThumbnail()).isEqualTo(thumbnail1);
            softly.assertThat(themes.get(1).getName()).isEqualTo(name2);
            softly.assertThat(themes.get(1).getDescription()).isEqualTo(description2);
            softly.assertThat(themes.get(1).getThumbnail()).isEqualTo(thumbnail2);
        });
    }

    @Test
    void 테마_이름은_중복_될_수_없다() {
        // given
        final String name = "우가우가";
        final String description = "우가우가 설명";
        final String thumbnail = "따봉우가.jpg";
        final Theme theme = new Theme(name, description, thumbnail);
        themeRepository.save(theme);

        // when & then
        Assertions.assertThatThrownBy(() -> {
            themeService.save(name, description, thumbnail);
        }).isInstanceOf(DataExistException.class);
    }
}
