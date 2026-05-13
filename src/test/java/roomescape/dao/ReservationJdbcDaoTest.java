package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.row.ReservationRow;
import roomescape.dao.row.ThemeRow;
import roomescape.dao.row.TimeRow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@JdbcTest
@Import({
        ReservationJdbcDao.class,
        TimeJdbcDao.class,
        ThemeJdbcDao.class
})
@ActiveProfiles("test")
class ReservationJdbcDaoTest {
    private static final int DELETED = 1;

    private static final LocalDate DATE = LocalDate.of(2026, 5, 10);
    private static final Long NOT_EXISTS_ID = Long.MAX_VALUE;

    @Autowired private ReservationDao reservationDao;
    @Autowired private TimeDao timeDao;
    @Autowired private ThemeDao themeDao;

    private TimeRow givenTime(int hour) {
        return timeDao.create(new TimeRow(LocalTime.of(hour, 0)));
    }

    private ThemeRow givenTheme(String name) {
        return themeDao.create(new ThemeRow(name, "url", "desc"));
    }

    private ReservationRow row(String name, LocalDate date, TimeRow time, ThemeRow theme) {
        return new ReservationRow(name, date, time, theme);
    }

    @Nested
    @DisplayName("create는")
    class Create {

        @Test
        void 저장_후_ID가_채워진_객체를_반환한다() {
            TimeRow time = givenTime(10);
            ThemeRow theme = givenTheme("방탈출");

            ReservationRow saved = reservationDao.create(row("유저", DATE, time, theme));

            assertThat(saved.id()).isNotNull();
            assertThat(saved.name()).isEqualTo("유저");
        }

        @Test
        void 같은_theme_time_date_조합이면_DuplicateKeyException() {
            TimeRow time = givenTime(10);
            ThemeRow theme = givenTheme("방탈출");
            reservationDao.create(row("유저1", DATE, time, theme));

            assertThatThrownBy(() ->
                    reservationDao.create(row("유저2", DATE, time, theme)))
                    .isInstanceOf(DuplicateKeyException.class);
        }

        @Test
        void 같은_theme_time이지만_date가_다르면_저장된다() {
            TimeRow time = givenTime(10);
            ThemeRow theme = givenTheme("방탈출");
            reservationDao.create(row("유저1", DATE, time, theme));

            assertThatCode(() ->
                    reservationDao.create(row("유저2", DATE.plusDays(1), time, theme))
            ).doesNotThrowAnyException();
        }

        @Test
        void 같은_theme_date이지만_time이_다르면_저장된다() {
            TimeRow time1 = givenTime(10);
            TimeRow time2 = givenTime(12);
            ThemeRow theme = givenTheme("방탈출");
            reservationDao.create(row("유저1", DATE, time1, theme));

            assertThatCode(() ->
                    reservationDao.create(row("유저2", DATE, time2, theme))
            ).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("findById는")
    class FindById {

        @Test
        void 존재하면_Optional로_감싸_반환한다() {
            TimeRow time = givenTime(10);
            ThemeRow theme = givenTheme("방탈출");
            ReservationRow saved = reservationDao.create(row("달수", DATE, time, theme));

            Optional<ReservationRow> found = reservationDao.findById(saved.id());

            assertThat(found).isPresent()
                    .get()
                    .isEqualTo(saved);
        }

        @Test
        void 존재하지_않으면_Optional_empty() {
            assertThat(reservationDao.findById(NOT_EXISTS_ID)).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAll은")
    class FindAll {

        @Test
        void 비어있으면_빈_리스트() {
            assertThat(reservationDao.findAll()).isEmpty();
        }

        @Test
        void 저장된_모든_예약을_반환한다() {
            TimeRow time1 = givenTime(10);
            TimeRow time2 = givenTime(12);
            ThemeRow theme1 = givenTheme("테마1");
            ThemeRow theme2 = givenTheme("테마2");

            ReservationRow first = reservationDao.create(row("유저1", DATE, time1, theme1));
            ReservationRow second = reservationDao.create(row("유저2", DATE, time2, theme2));

            List<ReservationRow> all = reservationDao.findAll();

            assertThat(all).hasSize(2).contains(first, second);
        }
    }

    @Nested
    @DisplayName("delete는")
    class Delete {

        @Test
        void 존재하는_ID는_1를_반환하고_삭제한다() {
            TimeRow time = givenTime(10);
            ThemeRow theme = givenTheme("방탈출");
            ReservationRow saved = reservationDao.create(row("달수", DATE, time, theme));

            int affected = reservationDao.delete(saved.id());

            assertThat(affected).isEqualTo(1);
            assertThat(reservationDao.findById(saved.id())).isEmpty();
        }

        @Test
        void 존재하지_않는_ID는_0을_반환한다() {
            assertThat(reservationDao.delete(NOT_EXISTS_ID)).isZero();
        }


        @Test
        void 삭제_후_같은_조합으로_재예약_가능() {
            TimeRow time = givenTime(10);
            ThemeRow theme = givenTheme("방탈출");
            ReservationRow saved = reservationDao.create(row("유저1", DATE, time, theme));
            reservationDao.delete(saved.id());

            assertThatCode(() ->
                    reservationDao.create(row("유저2", DATE, time, theme))
            ).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("existsBy는")
    class ExistsBy {

        @Test
        void existsById_저장된_ID는_true() {
            TimeRow time = givenTime(10);
            ThemeRow theme = givenTheme("방탈출");
            ReservationRow saved = reservationDao.create(row("달수", DATE, time, theme));

            assertThat(reservationDao.existsById(saved.id())).isTrue();
        }

        @Test
        void existsById_없는_ID는_false() {
            assertThat(reservationDao.existsById(NOT_EXISTS_ID)).isFalse();
        }

        @Test
        void existsByThemeIdAndTimeIdAndDate_조합_일치하면_true() {
            TimeRow time = givenTime(10);
            ThemeRow theme = givenTheme("방탈출");
            reservationDao.create(row("달수", DATE, time, theme));

            assertThat(reservationDao.existsByThemeIdAndTimeIdAndDate(theme.id(), time.id(), DATE))
                    .isTrue();
        }

        @Test
        void existsByThemeIdAndTimeIdAndDate_date만_달라도_false() {
            TimeRow time = givenTime(10);
            ThemeRow theme = givenTheme("방탈출");
            reservationDao.create(row("달수", DATE, time, theme));

            assertThat(reservationDao.existsByThemeIdAndTimeIdAndDate(
                    theme.id(), time.id(), DATE.plusDays(1))).isFalse();
        }

        @Test
        void existsByThemeId_예약이_있으면_true() {
            TimeRow time = givenTime(10);
            ThemeRow theme = givenTheme("방탈출");
            reservationDao.create(row("달수", DATE, time, theme));

            assertThat(reservationDao.existsByThemeId(theme.id())).isTrue();
        }

        @Test
        void existsByThemeId_예약이_없으면_false() {
            assertThat(reservationDao.existsByThemeId(NOT_EXISTS_ID)).isFalse();
        }

        @Test
        void existsByTimeId_예약이_있으면_true() {
            TimeRow time = givenTime(10);
            ThemeRow theme = givenTheme("방탈출");
            reservationDao.create(row("달수", DATE, time, theme));

            assertThat(reservationDao.existsByTimeId(time.id())).isTrue();
        }

        @Test
        void existsByTimeId_예약이_없으면_false() {
            assertThat(reservationDao.existsByTimeId(NOT_EXISTS_ID)).isFalse();
        }
    }
}
