package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Theme;

@JdbcTest
@Import(ThemeDao.class)
class ThemeDaoTest {

    @Autowired
    private ThemeDao themeDao;

    @Test
    void 테마를_등록한다() {
        // given
        String name = "방탈출1";
        String description = "로지와 러키의 신나는 방탈출";
        String thumbnail = "https://abc.asdfdsa";
        Theme theme = Theme.createWithoutId(name, description, thumbnail);

        // when
        Theme savedTheme = themeDao.insert(theme);

        // then
        assertAll(
                () -> assertThat(savedTheme.getId()).isNotNull(),
                () -> assertThat(savedTheme.getName()).isEqualTo(name),
                () -> assertThat(savedTheme.getDescription()).isEqualTo(description),
                () -> assertThat(savedTheme.getThumbnail()).isEqualTo(thumbnail)
        );
    }

    @Test
    void 아이디에_맞는_테마를_조회한다() {
        String name = "방탈출1";
        String description = "로지와 러키의 신나는 방탈출";
        String thumbnail = "https://abc.asdfdsa";
        Theme theme = Theme.createWithoutId(name, description, thumbnail);
        Theme savedTheme = themeDao.insert(theme);

        // when
        Optional<Theme> foundTheme = themeDao.selectById(savedTheme.getId());

        // then
        assertAll(
                () -> assertThat(foundTheme.isPresent()).isTrue(),
                () -> assertThat(foundTheme.get().getId()).isEqualTo(savedTheme.getId()),
                () -> assertThat(foundTheme.get().getName()).isEqualTo(name),
                () -> assertThat(foundTheme.get().getDescription()).isEqualTo(description),
                () -> assertThat(foundTheme.get().getThumbnail()).isEqualTo(thumbnail)
        );
    }

    @Test
    void 아이디에_맞는_테마가_없으면_빈_객체를_반환한다() {
        // when
        Optional<Theme> foundTheme = themeDao.selectById(1L);

        // then
        assertThat(foundTheme).isEmpty();
    }

//    @Test
//    void 테마를_삭제한다() {
//        // given
//        String name = "방탈출1";
//        String description = "로지와 러키의 신나는 방탈출";
//        String thumbnail = "https://abc.asdfdsa";
//        Theme theme = Theme.createWithoutId(name, description, thumbnail);
//        Theme savedTheme = themeDao.insert(theme);
//
//        // when
//        themeDao.delete(savedTheme.getId());
//
//        // then
//    }
}
