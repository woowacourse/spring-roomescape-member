package roomescape.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.row.AvailableTimeRow;
import roomescape.dao.row.ReservationRow;
import roomescape.dao.row.ThemeRow;
import roomescape.dao.row.TimeRow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@JdbcTest
@Import({
        ThemeJdbcDao.class,
        ReservationJdbcDao.class,
        TimeJdbcDao.class
})
@ActiveProfiles("test")
class ThemeJdbcDaoTest {
    
    private static final LocalDate DATE = LocalDate.of(2026, 5, 10);
    private static final Long NOT_EXISTS_ID = Long.MAX_VALUE;

    @Autowired private ThemeDao themeDao;
    @Autowired private ReservationDao reservationDao;
    @Autowired private TimeDao timeDao;

    private TimeRow givenTime(int hour) {
        return timeDao.create(new TimeRow(LocalTime.of(hour, 0)));
    }

    private ThemeRow givenTheme(String name) {
        return themeDao.create(new ThemeRow(name, "url", "desc"));
    }

    private ReservationRow givenReservation(String name, LocalDate date, TimeRow time, ThemeRow theme) {
        return reservationDao.create(new ReservationRow(name, date, time, theme));
    }

    private ThemeRow themeRow(String name, String thumbnailUrl, String description) {
        return new ThemeRow(name, thumbnailUrl, description);
    }

    @Nested
    @DisplayName("create는 ")
    class Create {

        @Test
        void 저장_후_ID가_채워진_객체를_반환한다() {
            ThemeRow theme = themeRow("테마", "www.test.com", "테스트 테마입니다.");

            ThemeRow saved = themeDao.create(theme);

            assertThat(saved.id()).isNotNull();
            assertThat(saved.name()).isEqualTo("테마");
        }

        @Test
        void 같은_name이면_DuplicateException() {
            ThemeRow theme = themeRow("테마", "www.test.com", "테스트 테마입니다.");

            themeDao.create(theme);

            assertThatThrownBy(() -> themeDao.create(theme))
                    .isInstanceOf(DuplicateKeyException.class);
        }
    }

    @Nested
    @DisplayName("findById는 ")
    class FindById {

        @Test
        void 존재하면_Optinal로_감싸_반환한다() {
            ThemeRow theme = themeRow("테마", "www.test.com", "테스트 테마입니다.");
            ThemeRow saved = themeDao.create(theme);

            Optional<ThemeRow> found = themeDao.findById(saved.id());

            assertThat(found).isPresent()
                    .get()
                    .isEqualTo(saved);
        }

        @Test
        void 존재하지_않으면_Optional_empty() {
            assertThat(themeDao.findById(NOT_EXISTS_ID)).isEmpty();
        }
    }

    @Nested
    @DisplayName("findAll은 ")
    class FindAll {

        @Test
        void 저장된_모든_테마를_반환한다() {
            ThemeRow theme1 = themeRow("테마1", "www.test1.com", "테스트 테마1입니다.");
            ThemeRow theme2 = themeRow("테마2", "www.test2.com", "테스트 테마2입니다.");

            ThemeRow first = themeDao.create(theme1);
            ThemeRow second = themeDao.create(theme2);

            List<ThemeRow> all = themeDao.findAll();

            assertThat(all).hasSize(2).contains(first, second);
        }

        @Test
        void 비어있으면_빈_리스트() {
            assertThat(themeDao.findAll()).isEmpty();
        }
    }

    @Nested
    @DisplayName("delete는 ")
    class Delete {

        @Test
        void 존재하는_ID는_1를_반환하고_삭제한다() {
            ThemeRow theme = themeRow("테마", "www.test.com", "테스트 테마입니다.");
            ThemeRow saved = themeDao.create(theme);

            int affected = themeDao.delete(saved.id());

            assertThat(affected).isEqualTo(1);
            assertThat(themeDao.findById(saved.id())).isEmpty();
        }

        @Test
        void 존재하지_않는_ID는_0을_반환한다() {
            assertThat(themeDao.delete(NOT_EXISTS_ID)).isZero();
        }

        @Test
        void 삭제_후_같은_조합으로_재예약_가능() {
            ThemeRow theme = themeRow("테마", "www.test.com", "테스트 테마입니다.");
            ThemeRow saved = themeDao.create(theme);
            themeDao.delete(saved.id());

            assertThatCode(() -> themeDao.create(theme))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("existBy는 ")
    class ExistSBy {

        @Test
        void existsById_저장된_ID는_true() {
            ThemeRow theme = themeRow("테마", "www.test.com", "테스트 테마입니다.");
            ThemeRow saved = themeDao.create(theme);

            assertThat(themeDao.existsById(saved.id())).isTrue();
        }

        @Test
        void existsById_없는_ID는_false() {
            assertThat(themeDao.existsById(NOT_EXISTS_ID)).isFalse();
        }

        @Test
        void existsByName_저장된_이름은_true() {
            ThemeRow theme = themeRow("테마", "www.test.com", "테스트 테마입니다.");
            ThemeRow saved = themeDao.create(theme);

            assertThat(themeDao.existsByName(saved.name())).isTrue();
        }

        @Test
        void existsByName_없는_이름은_false() {
            assertThat(themeDao.existsByName("없는 테마 이름")).isFalse();
        }
    }

    @Nested
    @DisplayName("findAvailableTimesById는 ")
    class FindAvailableTimesById {

        @Test
        void 다른_테마나_다른_날짜의_예약은_무시한다() {
            TimeRow time10 = givenTime(10);
            TimeRow time11 = givenTime(11);

            ThemeRow targetTheme = givenTheme("타겟 테마");
            ThemeRow otherTheme = givenTheme("다른 테마");

            givenReservation("유저A", DATE, time10, otherTheme);
            givenReservation("유저B", DATE.plusDays(1), time10, targetTheme);
            givenReservation("유저C", DATE, time11, targetTheme);

            List<AvailableTimeRow> result = themeDao.findAvailableTimesById(targetTheme.id(), DATE);

            assertThat(result)
                    .extracting(AvailableTimeRow::id, AvailableTimeRow::alreadyBooked)
                    .containsExactly(
                            tuple(time10.id(), false),
                            tuple(time11.id(), true)
                    );
        }
    }

    @Nested
    @DisplayName("findPopulars는")
    class FindPopulars {

        @Test
        void 기간_밖의_예약은_인기도_집계에서_제외한다() {
            ThemeRow themeA = givenTheme("테마A");
            ThemeRow themeB = givenTheme("테마B");

            TimeRow time = givenTime(10);

            int days = 7;
            int limit = 10;

            givenReservation("유저1", DATE.minusDays(3), time, themeA);
            givenReservation("유저2", DATE.minusDays(30), time, themeA);
            givenReservation("유저3", DATE.minusDays(31), time, themeA);
            givenReservation("유저4", DATE.minusDays(32), time, themeA);
            givenReservation("유저5", DATE.minusDays(33), time, themeA);
            givenReservation("유저6", DATE.minusDays(34), time, themeA);

            givenReservation("유저7", DATE.minusDays(1), time, themeB);
            givenReservation("유저8", DATE.minusDays(2), time, themeB);
            givenReservation("유저9", DATE.minusDays(3), time, themeB);

            List<ThemeRow> result = themeDao.findPopulars(limit, days, DATE);

            assertThat(result)
                    .extracting(ThemeRow::name)
                    .containsExactly("테마B", "테마A");
        }
    }
}
