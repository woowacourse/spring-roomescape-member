package roomescape.dao.reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationQueryDao;
import roomescape.dao.ThemeDao;
import roomescape.dao.TimeDao;
import roomescape.dao.row.AvailableTimeRow;
import roomescape.dao.row.ReservationRow;
import roomescape.dao.row.ThemeRow;
import roomescape.dao.row.TimeRow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;


public abstract class ReservationQueryDaoContract {

    private static final LocalDate DATE = LocalDate.of(2026, 5, 10);

    @BeforeEach
    void cleanUp() {
        clear();
    }

    void clear() {
        clearReservation();
        clearTheme();
        clearTime();
    }

    abstract void clearTime();

    abstract void clearTheme();

    abstract void clearReservation();

    abstract ReservationQueryDao queryDao();

    abstract TimeDao timeDao();

    abstract ThemeDao themeDao();

    abstract ReservationDao reservationDao();

    private TimeRow givenTime(int hour) {
        return timeDao().create(new TimeRow(LocalTime.of(hour, 0)));
    }

    private ThemeRow givenTheme(String name) {
        return themeDao().create(new ThemeRow(name, "url", "desc"));
    }

    private ReservationRow givenReservation(String name, LocalDate date, TimeRow time, ThemeRow theme) {
        return reservationDao().create(new ReservationRow(name, date, time, theme));
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

            List<AvailableTimeRow> result = queryDao().findAvailableTimesById(targetTheme.id(), DATE);

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

            List<ThemeRow> result = queryDao().findPopulars(limit, days, DATE);

            assertThat(result)
                    .extracting(ThemeRow::name)
                    .containsExactly("테마B", "테마A");
        }
    }
}
