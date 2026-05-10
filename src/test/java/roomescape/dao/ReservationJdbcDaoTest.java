package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.row.ReservationRow;
import roomescape.dao.row.ThemeRow;
import roomescape.dao.row.TimeRow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({
        ReservationJdbcDao.class,
        TimeJdbcDao.class,
        ThemeJdbcDao.class
})
@ActiveProfiles("test")
class ReservationJdbcDaoTest {
    private static final int DELETED = 1;

    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private TimeDao timeDao;
    @Autowired
    private ThemeDao themeDao;

    private ReservationRow reservaton1;
    private ReservationRow reservaton2;

    @BeforeEach
    void setUp() {
        TimeRow time1 = timeDao.create(new TimeRow(LocalTime.parse("10:00")));
        TimeRow time2 = timeDao.create(new TimeRow(LocalTime.parse("12:00")));

        ThemeRow theme1 = themeDao.create(new ThemeRow("방 이름", "url", "설명"));
        ThemeRow theme2 = themeDao.create(new ThemeRow("두번째 방이름", "url2", "설명2"));
        reservaton1 = new ReservationRow("이름1", LocalDate.parse("2026-05-05"), time1, theme1);
        reservaton2 = new ReservationRow("이름2", LocalDate.parse("2026-05-06"), time2, theme2);
    }

    @Test
    void findAll() {
        List<ReservationRow> saved = createReservationsHandler(reservaton1, reservaton2);

        List<ReservationRow> find = reservationDao.findAll();

        assertThat(find).hasSize(saved.size())
                .containsAll(saved);
    }

    @Test
    void findById() {
        ReservationRow saved = createReservationHandler(reservaton1);

        assertThat(reservationDao.findById(saved.id()))
                .isPresent()
                .get().isEqualTo(saved);
    }

    @Test
    void create() {
        ReservationRow saved = reservationDao.create(reservaton1);
        assertThat(saved).isNotNull();
    }

    @Test
    void delete() {
        ReservationRow saved = createReservationHandler(reservaton1);
        assertThat(reservationDao.delete(saved.id())).isEqualTo(DELETED);
    }

    @Test
    void existsByThemeIdAndTimeIdAndDate() {
        ReservationRow saved = createReservationHandler(reservaton1);
        ReservationRow notExists = reservaton2;

        assertThat(reservationDao.existsByThemeIdAndTimeIdAndDate(saved.themeRow().id(), saved.timeRow().id(),
                saved.date())).isTrue();

        assertThat(reservationDao.existsByThemeIdAndTimeIdAndDate(notExists.themeRow().id(),
                notExists.timeRow().id(), notExists.date())).isFalse();
    }

    @Test
    void existsById() {
        ReservationRow saved = createReservationHandler(reservaton1);
        ReservationRow notExists = reservaton2;

        assertThat(reservationDao.existsById(saved.id())).isTrue();

        assertThat(reservationDao.existsById(notExists.id())).isFalse();
    }

    private List<ReservationRow> createReservationsHandler(ReservationRow... reservations) {
        return Arrays.stream(reservations)
                .map(this::createReservationHandler)
                .toList();
    }

    private ReservationRow createReservationHandler(ReservationRow reservation) {
        return reservationDao.create(reservation);
    }

}
