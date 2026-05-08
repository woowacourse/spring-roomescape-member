package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.jdbc.ReservationJdbcDao;
import roomescape.dao.jdbc.ThemeJdbcDao;
import roomescape.dao.jdbc.TimeJdbcDao;
import roomescape.domain.Reservation;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.vo.Name;

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

    private Reservation reservaton1;
    private Reservation reservaton2;

    @BeforeEach
    void setUp() {
        Time time1 = timeDao.insert(new Time(LocalTime.parse("10:00")));
        Time time2 = timeDao.insert(new Time(LocalTime.parse("12:00")));

        Theme theme1 = themeDao.insert(new Theme(new Name("방 이름"), "url", "설명"));
        Theme theme2 = themeDao.insert(new Theme(new Name("두번째 방이름"), "url2", "설명2"));
        reservaton1 = new Reservation("이름1", LocalDate.parse("2026-05-05"), time1, theme1);
        reservaton2 = new Reservation("이름2", LocalDate.parse("2026-05-06"), time2, theme2);
    }

    @Test
    void findAll() {
        List<Reservation> saved = insertReservationsHandler(reservaton1, reservaton2);

        List<Reservation> find = reservationDao.findAll();

        assertThat(find).hasSize(saved.size())
                .containsAll(saved);
    }

    @Test
    void findById() {
        Reservation saved = insertReservationHandler(reservaton1);

        assertThat(reservationDao.findById(saved.getId()))
                .isPresent()
                .get().isEqualTo(saved);
    }

    @Test
    void insert() {
        Reservation saved = reservationDao.insert(reservaton1);
        assertThat(saved).isNotNull();
    }

    @Test
    void delete() {
        Reservation saved = reservationDao.insert(reservaton1);
        assertThat(reservationDao.delete(saved.getId())).isEqualTo(DELETED);
    }

    private List<Reservation> insertReservationsHandler(Reservation... reservations) {
        return Arrays.stream(reservations)
                .map(this::insertReservationHandler)
                .toList();
    }

    private Reservation insertReservationHandler(Reservation reservation) {
        return reservationDao.insert(reservation);
    }

    @Test
    void existsByThemeIdAndTimeIdAndDate() {
        Reservation saved = insertReservationHandler(reservaton1);
        Reservation notExists = reservaton2;

        assertThat(reservationDao.existsByThemeIdAndTimeIdAndDate(saved.getTheme().getId(), saved.getTime().getId(),
                saved.getDate())).isTrue();

        assertThat(reservationDao.existsByThemeIdAndTimeIdAndDate(notExists.getTheme().getId(),
                notExists.getTime().getId(), notExists.getDate())).isFalse();
    }
}
