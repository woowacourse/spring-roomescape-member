package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
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
class ReservationJdbcDaoTest {
    private static final int DELETED = 1;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private TimeDao timeDao;
    @Autowired
    private ThemeDao themeDao;

    @BeforeEach
    void setUp() {
        timeDao.insert(new Time(LocalTime.parse("10:00")));
        timeDao.insert(new Time(LocalTime.parse("12:00")));

        themeDao.insert(new Theme(new Name("방 이름"), "url", "설명"));
        themeDao.insert(new Theme(new Name("두번째 방이름"), "url2", "설명2"));
    }

    @ParameterizedTest
    @CsvSource("1,2,3")
    void findAll(int count) {
        for (int i = 0; i < count; i++) {
            createAndInsertReservation();
        }
        List<Reservation> reservations = reservationDao.findAll();
        assertThat(reservations)
                .hasSize(count);
    }

    @Test
    void findById() {
        Long andInsertReservation = createAndInsertReservation();

        assertThat(reservationDao.findById(andInsertReservation)).isPresent();
    }

    @Test
    void insert() {
        Time time = timeDao.findAll().stream().findFirst().get();
        Theme theme = themeDao.findAll().stream().findFirst().get();
        Reservation reservation = new Reservation("이름", LocalDate.parse("2026-05-05"), time, theme);

        assertThat(reservationDao.insert(reservation)).isNotNull();
    }

    @Test
    void delete() {
        Long savedId = createAndInsertReservation();

        assertThat(reservationDao.delete(savedId)).isEqualTo(DELETED);
    }


    public Long createAndInsertReservation() {
        Time time = timeDao.findAll().stream().findFirst().get();
        Theme theme = themeDao.findAll().stream().findFirst().get();
        return reservationDao.insert(new Reservation("이름", LocalDate.parse("2026-05-05"), time, theme));
    }
}
