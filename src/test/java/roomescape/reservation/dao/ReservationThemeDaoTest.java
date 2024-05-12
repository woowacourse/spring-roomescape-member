package roomescape.reservation.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dao.reservation.ReservationThemeDao;
import roomescape.domain.reservation.ReservationTheme;

@JdbcTest
@Import(ReservationThemeDao.class)
@Sql(scripts = {"/test_schema.sql"})
public class ReservationThemeDaoTest {

    @Autowired
    private ReservationThemeDao themeDao;

    @DisplayName("모든 테마를 조회한다.")
    @Test
    void findAll() {
        // given
        themeDao.insert(new ReservationTheme(null, "name", "desc", "thumb"));

        // when
        List<ReservationTheme> reservationThemes = themeDao.findAll();

        // then
        assertThat(reservationThemes.size()).isEqualTo(1);
    }

    @DisplayName("테마 ID를 이용하여 테마를 조회한다.")
    @Test
    void findById() {
        // given
        ReservationTheme inserted = themeDao.insert(new ReservationTheme(null, "name", "desc", "thumb"));

        // when
        ReservationTheme theme = themeDao.findById(inserted.getId()).orElseThrow();

        // then
        assertThat(theme.getName()).isEqualTo(inserted.getName());
    }

    @DisplayName("ID가 존재하지 않으면 빈 시간을 반환한다.")
    @Test
    void findByWrongId() {
        Optional<ReservationTheme> reservationTheme = themeDao.findById(9L);

        assertThat(reservationTheme).isEmpty();
    }

    @DisplayName("테마를 추가한다.")
    @Test
    void insert() {
        // given
        ReservationTheme inserted = themeDao.insert(new ReservationTheme(null, "name", "desc", "thumb"));

        // when
        Long insertedThemeId = inserted.getId();

        // then
        assertThat(insertedThemeId).isEqualTo(1L);
    }

    @DisplayName("ID를 이용하여 테마를 삭제한다.")
    @Test
    void deleteById() {
        // given
        ReservationTheme inserted = themeDao.insert(new ReservationTheme(null, "name", "desc", "thumb"));

        // when
        themeDao.deleteById(inserted.getId());
        Optional<ReservationTheme> theme = themeDao.findById(inserted.getId());

        // then
        assertThat(theme).isEmpty();
    }

    @DisplayName("ID를 이용하여 테마가 존재하는지 확인한다.")
    @Test
    void isExist() {
        // given
        ReservationTheme inserted = themeDao.insert(new ReservationTheme(null, "name", "desc", "thumb"));

        // when
        Boolean isExist = themeDao.isExist(inserted.getId());

        // then
        assertThat(isExist).isTrue();
    }

    @DisplayName("동일한 이름의 테마가 존재하는지 확인한다.")
    @Test
    void hasSameName() {
        // given
        ReservationTheme inserted = themeDao.insert(new ReservationTheme(null, "name", "desc", "thumb"));

        // when
        Boolean hasSameName = themeDao.hasSameName("name");

        // then
        assertThat(hasSameName).isTrue();
    }

    @DisplayName("지난 일주일간 가장 많이 예약된 테마를 조회한다.")
    @Test
    @Sql(scripts = {"/test_schema.sql", "/test_weekly_theme.sql"})
    void findBestThemeInWeek() {
        // given
        LocalDate from = LocalDate.now().minusWeeks(1);
        LocalDate to = LocalDate.now().minusDays(1);

        // when
        List<ReservationTheme> bestThemesInWeek = themeDao.findBestThemesInWeek(from, to);

        // then
        assertThat(bestThemesInWeek).extracting("name").containsExactly("test3", "test2", "test1", "test4");
    }
}
