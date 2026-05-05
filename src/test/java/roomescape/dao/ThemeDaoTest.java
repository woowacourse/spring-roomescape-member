package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
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
    @Sql(scripts = {"/data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"/truncate.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void 인기_테마_10개를_조회한다() {
        // given
        LocalDate startDate = LocalDate.of(2026, 4, 29);
        LocalDate endDate = LocalDate.of(2026, 5, 5);

        // when
        List<Theme> popularThemes = themeDao.selectPopularThemesByPeriod(startDate, endDate);

        // then
        assertAll(
                () -> assertThat(popularThemes.getFirst().getName()).isEqualTo("공포의 저택"),
                () -> assertThat(popularThemes.get(1).getName()).isEqualTo("사라진 연구소"),
                () -> assertThat(popularThemes.get(2).getName()).isEqualTo("시간 여행자"),
                () -> assertThat(popularThemes.get(3).getName()).isEqualTo("감옥 탈출"),
                () -> assertThat(popularThemes.get(4).getName()).isEqualTo("마법사의 방"),
                () -> assertThat(popularThemes.get(5).getName()).isEqualTo("좀비 바이러스"),
                () -> assertThat(popularThemes.get(6).getName()).isEqualTo("해적의 보물"),
                () -> assertThat(popularThemes.get(7).getName()).isEqualTo("스파이 미션"),
                () -> assertThat(popularThemes.get(8).getName()).isEqualTo("우주 정거장"),
                () -> assertThat(popularThemes.get(9).getName()).isEqualTo("고대 유적")
        );
    }
    //1. 공포의 저택       12건
    //2. 사라진 연구소     10건
    //3. 시간 여행자       9건
    //4. 감옥 탈출         8건
    //5. 마법사의 방       7건
    //6. 좀비 바이러스     6건
    //7. 해적의 보물       5건
    //8. 스파이 미션       4건
    //9. 우주 정거장       3건
    //10. 고대 유적        2건

    @Test
    void 아이디에_맞는_테마가_없으면_빈_객체를_반환한다() {
        // when
        Optional<Theme> foundTheme = themeDao.selectById(900L);

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
