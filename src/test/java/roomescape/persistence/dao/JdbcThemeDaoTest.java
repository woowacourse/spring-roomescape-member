package roomescape.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.domain.Theme;

@JdbcTest
class JdbcThemeDaoTest {

    private final ThemeDao themeDao;

    @Autowired
    JdbcThemeDaoTest(final JdbcTemplate jdbcTemplate) {
        this.themeDao = new JdbcThemeDao(jdbcTemplate);
    }

    @Test
    @DisplayName("데이터베이스에 테마를 저장하고 조회하여 확인한다")
    void saveAndFindById() {
        // given
        final Theme theme = new Theme("테마", "소개", "썸네일");

        // when
        final Long id = themeDao.save(theme);

        // then
        final Optional<Theme> findTheme = themeDao.find(id);
        assertAll(
                () -> assertThat(findTheme).isPresent(),
                () -> assertThat(findTheme.get().getId()).isEqualTo(id),
                () -> assertThat(findTheme.get().getName()).isEqualTo(theme.getName()),
                () -> assertThat(findTheme.get().getDescription()).isEqualTo(theme.getDescription()),
                () -> assertThat(findTheme.get().getThumbnail()).isEqualTo(theme.getThumbnail())
        );
    }

    @Test
    @DisplayName("데이터베이스에 테마를 삭제한다")
    void deleteById() {
        // given
        final Theme theme = new Theme("테마", "소개", "썸네일");
        final Long id = themeDao.save(theme);

        // when
        final boolean isDeleted = themeDao.remove(id);

        // then
        assertAll(
                () -> assertThat(isDeleted).isTrue(),
                () -> assertThat(themeDao.find(id)).isEmpty()
        );
    }

    @Test
    @DisplayName("데이터베이스의 모든 테마를 조회한다")
    void findAll() {
        // given
        final Theme theme1 = new Theme("테마1", "소개1", "썸네일1");
        final Theme theme2 = new Theme("테마2", "소개2", "썸네일2");
        themeDao.save(theme1);
        themeDao.save(theme2);

        // when
        final List<Theme> themes = themeDao.findAll();

        // then
        assertThat(themes).hasSize(2);
    }

    @Test
    @DisplayName("데이터베이스에서 id를 통해 테마를 삭제할 때 대상이 없다면 false 반환한다")
    void deleteByIdWhenNotExist() {
        // given
        final Long notExistId = 999L;

        // when
        final boolean isDeleted = themeDao.remove(notExistId);

        // then
        assertThat(isDeleted).isFalse();
    }
}
