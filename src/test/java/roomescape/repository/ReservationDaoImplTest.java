package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.entity.Reservation;
import roomescape.entity.ReservationTime;
import roomescape.repository.ReservationDaoImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@Sql("/reservationTimeInsert.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationDaoImplTest {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private ReservationDaoImpl reservationDao;

    @BeforeEach
    void setUp() {
        reservationDao = new ReservationDaoImpl(jdbcTemplate);
    }

    @Test
    void 예약을_저장한다() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        Reservation reservation = new Reservation(
            "drago",
            LocalDate.of(2025, 5, 1),
            new ReservationTime(1L, time)
        );

        // when
        Reservation savedReservation = reservationDao.save(reservation);

        // then
        Reservation expected = new Reservation(
            1L,
            "drago",
            LocalDate.of(2025, 5, 1),
            new ReservationTime(1L, LocalTime.of(10, 0))
        );
        assertThat(savedReservation).isEqualTo(expected);
    }

    @Test
    void 모든_예약을_조회한다() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        Reservation drago = reservationDao.save(new Reservation("drago",
            LocalDate.of(2025, 5, 1),
            new ReservationTime(1L, time)));

        Reservation cookie = reservationDao.save(new Reservation("cookie",
            LocalDate.of(2025, 5, 2),
            new ReservationTime(1L, time)));

        // when
        List<Reservation> all = reservationDao.findAll();

        // then
        assertThat(all).containsExactly(drago, cookie);
    }

    @Test
    void 예약을_삭제한다() {
        // given
        LocalTime time = LocalTime.of(10, 0);
        Reservation drago = reservationDao.save(new Reservation("drago",
            LocalDate.of(2025, 5, 1),
            new ReservationTime(1L, time)));

        // when
        reservationDao.deleteById(drago.getId());

        // then
        List<Reservation> all = reservationDao.findAll();
        assertThat(all.isEmpty()).isTrue();
    }
}